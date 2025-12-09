package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "DTO истории проектов команды")
public record ProjectHistoryDto(

        @Schema(description = "ID проекта")
        Long projectId,

        @Schema(description = "Название проекта")
        String projectTitle,
        
        @Schema(description = "ID семестра")
        Long semesterId,

        @Schema(description = "Название семестра")
        String semesterName,

        @Schema(description = "Дата назначения")
        LocalDateTime assignedAt,

        @Schema(description = "Дата снятия")
        LocalDateTime unassignedAt,

        @Schema(description = "Активен сейчас")
        boolean isActive
) {
}
