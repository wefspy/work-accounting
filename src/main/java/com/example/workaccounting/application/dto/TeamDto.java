package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "DTO команды")
public record TeamDto(

        @Schema(description = "ID команды")
        Long id,

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Описание команды")
        String description,

        @Schema(description = "ID создателя")
        Long createdById,

        @Schema(description = "ФИО создателя")
        String createdByName,

        @Schema(description = "Дата создания")
        LocalDateTime createdAt,

        @Schema(description = "Список участников")
        List<ParticipantDto> participants,

        @Schema(description = "ID текущего проекта")
        Long currentProjectId,

        @Schema(description = "Название текущего проекта")
        String currentProjectTitle,

        @Schema(description = "История проектов")
        List<ProjectHistoryDto> projectHistory
) {
}
