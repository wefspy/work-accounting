package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.yandex.YandexEventDto;
import com.example.workaccounting.application.dto.yandex.YandexEventInputDto;
import com.example.workaccounting.application.service.YandexCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar/events")
@RequiredArgsConstructor
@Tag(name = "Яндекс Календарь", description = "Операции с Яндекс Календарем")
public class YandexCalendarRestController {

    private final YandexCalendarService yandexCalendarService;

    @GetMapping
    @Operation(summary = "Получить события", description = "Получение списка событий из Яндекс Календаря за указанный период")
    public List<YandexEventDto> getEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        return yandexCalendarService.getEvents(from, to);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать событие", description = "Создание нового события в Яндекс Календаре")
    public void createEvent(@RequestBody YandexEventInputDto eventDto) {
        yandexCalendarService.createEvent(eventDto);
    }

    @PutMapping("/{uid}")
    @Operation(summary = "Обновить событие", description = "Обновление существующего события в Яндекс Календаре")
    public void updateEvent(@PathVariable String uid, @RequestBody YandexEventInputDto eventDto) {
        yandexCalendarService.updateEvent(uid, eventDto);
    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "Удалить событие", description = "Удаление события из Яндекс Календаря")
    public void deleteEvent(@PathVariable String uid) {
        yandexCalendarService.deleteEvent(uid);
    }
}
