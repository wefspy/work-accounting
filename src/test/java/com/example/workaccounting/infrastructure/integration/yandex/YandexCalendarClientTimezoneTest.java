package com.example.workaccounting.infrastructure.integration.yandex;

import com.example.workaccounting.infrastructure.properties.YandexProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import com.example.workaccounting.application.dto.yandex.YandexEventDto;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class YandexCalendarClientTimezoneTest {

    @Test
    public void testToUtc() throws Exception {
        YandexCalendarClient client = new YandexCalendarClient(new YandexProperties());
        Method toUtcMethod = YandexCalendarClient.class.getDeclaredMethod("toUtc", ZonedDateTime.class);
        toUtcMethod.setAccessible(true);

        ZonedDateTime input = ZonedDateTime.of(2026, 1, 16, 12, 0, 55, 0, ZoneId.of("UTC"));
        String result = (String) toUtcMethod.invoke(client, input);

        // Expected: 20260116T120055Z
        Assertions.assertEquals("20260116T120055Z", result);
    }

    @Test
    public void testParseDate() throws Exception {
        YandexCalendarClient client = new YandexCalendarClient(new YandexProperties());
        Method parseDateMethod = YandexCalendarClient.class.getDeclaredMethod("parseDate", String.class);
        parseDateMethod.setAccessible(true);

        String input = "20260116T120055Z";
        ZonedDateTime result = (ZonedDateTime) parseDateMethod.invoke(client, input);

        // Expected: 2026-01-16T12:00:55Z
        Assertions.assertEquals(ZonedDateTime.of(2026, 1, 16, 12, 0, 55, 0, ZoneId.of("UTC")), result);
    }

    @Test
    public void testBuildIcsBodyWithRecurrence() throws Exception {
        YandexCalendarClient client = new YandexCalendarClient(new YandexProperties());
        Method buildIcsBodyMethod = YandexCalendarClient.class.getDeclaredMethod("buildIcsBody", YandexEventDto.class);
        buildIcsBodyMethod.setAccessible(true);

        String recurrence = "DTSTART:20260116T100000\nRRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR";
        YandexEventDto event = YandexEventDto.builder()
                .uid("test-uid")
                .summary("Recurring Event")
                .recurrence(recurrence)
                .build();

        String result = (String) buildIcsBodyMethod.invoke(client, event);

        Assertions.assertTrue(result.contains("DTSTART:20260116T100000"));
        Assertions.assertTrue(result.contains("RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR"));
        Assertions.assertFalse(result.contains("DTSTART:") && result.indexOf("DTSTART:") != result.lastIndexOf("DTSTART:")); // Should appear only once (from recurrence) if logic is correct? Actually my code checks if recurrence contains it, if so it appends it. It DOES NOT append another DTSTART if start is null.
        // Wait, if start is null, my code skips appending DTSTART.
        // And if recurrence contains DTSTART, it appends the recurrence string.
        // So valid.
    }
}
