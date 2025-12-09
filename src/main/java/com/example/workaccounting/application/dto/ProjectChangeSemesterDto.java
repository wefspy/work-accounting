package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для изменения семестра проекта")
public record ProjectChangeSemesterDto(
        @Schema(description = "ID семестра")
        Long semesterId
) {
}
