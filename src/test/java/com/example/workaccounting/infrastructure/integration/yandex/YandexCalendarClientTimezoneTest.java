package com.example.workaccounting.infrastructure.integration.yandex;

import com.example.workaccounting.infrastructure.properties.YandexProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
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
}
