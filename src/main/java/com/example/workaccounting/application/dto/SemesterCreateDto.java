package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "DTO для создания нового семестра")
public record SemesterCreateDto(

        @Schema(description = "Название семестра")
        String name,

        @Schema(description = "Дата начала")
        LocalDateTime startsAt,

        @Schema(description = "Дата окончания")
        LocalDateTime endsAt
) {
}
