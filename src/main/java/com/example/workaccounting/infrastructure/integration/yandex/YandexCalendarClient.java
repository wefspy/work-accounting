package com.example.workaccounting.infrastructure.integration.yandex;

import com.example.workaccounting.application.dto.yandex.YandexEventDto;
import com.example.workaccounting.infrastructure.properties.YandexProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class YandexCalendarClient {

    private final YandexProperties properties;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final DateTimeFormatter ICAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    private static final ZoneId ZONE_ID = ZoneId.of("UTC");

    public List<YandexEventDto> getEvents(ZonedDateTime from, ZonedDateTime to) {
        String authHeader = getAuthHeader();
        String reportBody = buildReportBody(from, to);
        String url = getCleanUrl();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("User-Agent", "WorkAccounting/1.0")
                .header("Depth", "1")
                .header("Content-Type", "application/xml; charset=utf-8")
                .method("REPORT", HttpRequest.BodyPublishers.ofString(reportBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 207 && response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch events from Yandex Calendar. Status: " + response.statusCode());
            }
            return parseEvents(response.body());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error fetching events", e);
        }
    }

    public void createEvent(YandexEventDto event) {
        String uid = UUID.randomUUID().toString();
        event.setUid(uid);
        String icsBody = buildIcsBody(event);
        String url = getCleanUrl() + uid + ".ics";

        sendPutRequest(url, icsBody);
    }

    public void updateEvent(String uid, YandexEventDto event) {
        event.setUid(uid);
        String icsBody = buildIcsBody(event);
        String url = getCleanUrl() + uid + ".ics";

        sendPutRequest(url, icsBody);
    }

    public void deleteEvent(String uid) {
        String url = getCleanUrl() + uid + ".ics";
        String authHeader = getAuthHeader();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("User-Agent", "WorkAccounting/1.0")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 204 && response.statusCode() != 200) {
                     throw new RuntimeException("Failed to delete event");
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error deleting event", e);
        }
    }

    private void sendPutRequest(String url, String body) {
        String authHeader = getAuthHeader();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("User-Agent", "WorkAccounting/1.0")
                .header("Content-Type", "text/calendar; charset=utf-8")
                .PUT(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201 && response.statusCode() != 204 && response.statusCode() != 200) {
                throw new RuntimeException("Failed to save event to Yandex Calendar");
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error saving event", e);
        }
    }

    private String getAuthHeader() {
        String auth = properties.getUser() + ":" + properties.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
    
    private String getCleanUrl() {
        String url = properties.getCaldavUrl();
        if (url == null) {
            throw new RuntimeException("Yandex CalDAV URL is not configured");
        }
        if (!url.endsWith("/")) {
            return url + "/";
        }
        return url;
    }

    private String buildReportBody(ZonedDateTime from, ZonedDateTime to) {
        String start = toUtc(from);
        String end = toUtc(to);

        return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<C:calendar-query xmlns:D=\"DAV:\" xmlns:C=\"urn:ietf:params:xml:ns:caldav\">\n" +
                "  <D:prop>\n" +
                "    <D:getetag/>\n" +
                "    <C:calendar-data/>\n" +
                "  </D:prop>\n" +
                "  <C:filter>\n" +
                "    <C:comp-filter name=\"VCALENDAR\">\n" +
                "      <C:comp-filter name=\"VEVENT\">\n" +
                "        <C:time-range start=\"" + start + "\" end=\"" + end + "\"/>\n" +
                "      </C:comp-filter>\n" +
                "    </C:comp-filter>\n" +
                "  </C:filter>\n" +
                "</C:calendar-query>";
    }

    private String buildIcsBody(YandexEventDto event) {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\r\n");
        sb.append("VERSION:2.0\r\n");
        sb.append("PRODID:-//WorkAccounting//NONSGML v1.0//EN\r\n");
        sb.append("BEGIN:VEVENT\r\n");
        sb.append("UID:").append(event.getUid()).append("\r\n");
        sb.append("DTSTAMP:").append(ZonedDateTime.now(ZoneId.of("UTC")).format(ICAL_DATE_FORMAT)).append("Z\r\n");
        sb.append("DTSTART:").append(toUtc(event.getStart())).append("\r\n");
        sb.append("DTEND:").append(toUtc(event.getEnd())).append("\r\n");
        sb.append("SUMMARY:").append(event.getSummary()).append("\r\n");
        if (event.getDescription() != null) {
            sb.append("DESCRIPTION:").append(event.getDescription()).append("\r\n");
        }
        if (event.getLocation() != null) {
            sb.append("LOCATION:").append(event.getLocation()).append("\r\n");
        }
        if (event.getRecurrence() != null) {
            sb.append("RRULE:").append(event.getRecurrence()).append("\r\n");
        }
        sb.append("END:VEVENT\r\n");
        sb.append("END:VCALENDAR\r\n");
        return sb.toString();
    }

    private List<YandexEventDto> parseEvents(String xmlBody) {
        List<YandexEventDto> events = new ArrayList<>();

        Pattern eventPattern = Pattern.compile("BEGIN:VEVENT(.*?)END:VEVENT", Pattern.DOTALL);
        Matcher matcher = eventPattern.matcher(xmlBody);

        while (matcher.find()) {
            String eventBlock = matcher.group(1);
            YandexEventDto dto = parseEventBlock(eventBlock);
            if(dto != null) {
                events.add(dto);
            }
        }
        return events;
    }

    private YandexEventDto parseEventBlock(String block) {
        try {
            String uid = extractValue(block, "UID:");
            String summary = extractValue(block, "SUMMARY:");
            String desc = extractValue(block, "DESCRIPTION:");
            String dtstart = extractValue(block, "DTSTART:");
            String dtend = extractValue(block, "DTEND:");
            String location = extractValue(block, "LOCATION:");
            String rrule = extractValue(block, "RRULE:");

            ZonedDateTime start = parseDate(dtstart);
            ZonedDateTime end = parseDate(dtend);

            return YandexEventDto.builder()
                    .uid(uid)
                    .summary(summary)
                    .description(desc)
                    .start(start)
                    .end(end)
                    .location(location)
                    .recurrence(rrule)
                    .build();
        } catch (Exception e) {
            log.warn("Failed to parse event block: {}", e.getMessage());
            return null;
        }
    }

    private String extractValue(String block, String prefix) {
        try (BufferedReader reader = new BufferedReader(new StringReader(block))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prefix) || (line.contains(":") && line.substring(0, line.indexOf(":")).contains(prefix.replace(":", "")))) {
                     int colIndex = line.indexOf(":");
                     return line.substring(colIndex + 1).trim();
                }
            }
        } catch (IOException e) {
            // ignore
        }
        return null;
    }

    private ZonedDateTime parseDate(String dateStr) {
        if (dateStr == null) return null;
        String cleanDate = dateStr.replaceAll("[^0-9TZ]", "");
        if (cleanDate.length() >= 15) {
             if (cleanDate.endsWith("Z")) {
                 // UTC time
                 LocalDateTime utcTime = LocalDateTime.parse(cleanDate.substring(0, 15), ICAL_DATE_FORMAT);
                 return utcTime.atZone(ZoneId.of("UTC"));
             } else {
                 // Floating time, assume Moscow as per previous logic or default to system?
                 // Since we moved to ZonedDateTime, let's treat floating as Moscow (historical default) or UTC?
                 // The user says "Widget uses device timezone UTC+5".
                 // Yandex usually returns floating time for non-UTC events or specific timezones.
                 // Let's stick to UTC if we treat everything as UTC now, or better:
                 // If no Z, it's floating. But we changed ZONE_ID to UTC.
                 return LocalDateTime.parse(cleanDate.substring(0, 15), ICAL_DATE_FORMAT).atZone(ZoneId.of("UTC"));
             }
        }
        return null;
    }

    private String toUtc(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return "";
        }
        return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))
                .format(ICAL_DATE_FORMAT) + "Z";
    }
}
