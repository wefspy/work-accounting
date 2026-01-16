package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.yandex.YandexEventDto;
import com.example.workaccounting.application.dto.yandex.YandexEventInputDto;
import com.example.workaccounting.infrastructure.integration.yandex.YandexCalendarClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexCalendarService {

    private final YandexCalendarClient yandexCalendarClient;

    public List<YandexEventDto> getEvents(ZonedDateTime from, ZonedDateTime to) {
        return yandexCalendarClient.getEvents(from, to);
    }

    public void createEvent(YandexEventInputDto eventDto) {
        YandexEventDto fullDto = YandexEventDto.builder()
                .summary(eventDto.getSummary())
                .description(eventDto.getDescription())
                .start(eventDto.getStart())
                .end(eventDto.getEnd())
                .location(eventDto.getLocation())
                .recurrence(eventDto.getRecurrence())
                .build();
        yandexCalendarClient.createEvent(fullDto);
    }

    public void updateEvent(String uid, YandexEventInputDto eventDto) {
        YandexEventDto fullDto = YandexEventDto.builder()
                .uid(uid)
                .summary(eventDto.getSummary())
                .description(eventDto.getDescription())
                .start(eventDto.getStart())
                .end(eventDto.getEnd())
                .location(eventDto.getLocation())
                .recurrence(eventDto.getRecurrence())
                .build();
        yandexCalendarClient.updateEvent(uid, fullDto);
    }

    public void deleteEvent(String uid) {
        yandexCalendarClient.deleteEvent(uid);
    }
}
