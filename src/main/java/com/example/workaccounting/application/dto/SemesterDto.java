package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "DTO семестра")
public record SemesterDto(
        @Schema(description = "ID семестра")
        Long id,

        @Schema(description = "Название семестра")
        String name,

        @Schema(description = "Дата начала")
        LocalDateTime startsAt,

        @Schema(description = "Дата окончания")
        LocalDateTime endsAt,

        @Schema(description = "Активен ли семестр")
        boolean isActive
) {
}
