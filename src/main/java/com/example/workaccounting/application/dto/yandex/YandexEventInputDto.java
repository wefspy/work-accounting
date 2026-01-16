package com.example.workaccounting.application.dto.yandex;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания/обновления события Яндекс Календаря")
public class YandexEventInputDto {
    @Schema(description = "Заголовок/Название события", requiredMode = Schema.RequiredMode.REQUIRED)
    private String summary;

    @Schema(description = "Описание события")
    private String description;

    @Schema(description = "Время начала события", requiredMode = Schema.RequiredMode.REQUIRED)
    private ZonedDateTime start;

    @Schema(description = "Время окончания события", requiredMode = Schema.RequiredMode.REQUIRED)
    private ZonedDateTime end;

    @Schema(description = "Место проведения события")
    private String location;

    @Schema(description = "Правило повторения (RRULE), например 'FREQ=WEEKLY;COUNT=5'")
    private String recurrence;
}
