package com.example.workaccounting.application.dto.yandex;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO события Яндекс Календаря")
public class YandexEventDto {
    @Schema(description = "Уникальный идентификатор события (UID)", accessMode = Schema.AccessMode.READ_ONLY)
    private String uid;

    @Schema(description = "Заголовок/Название события")
    private String summary;

    @Schema(description = "Описание события")
    private String description;

    @Schema(description = "Время начала события")
    private LocalDateTime start;

    @Schema(description = "Время окончания события")
    private LocalDateTime end;
}
