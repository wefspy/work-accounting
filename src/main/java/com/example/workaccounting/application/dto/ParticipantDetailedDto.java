package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Schema(description = "Подробная информация о участнике")
public record ParticipantDetailedDto(
        @Schema(description = "ID участника")
        Long id,

        @Schema(description = "ФИО")
        String fullname,

        @Schema(description = "Биография")
        String bio,

        @Schema(description = "Текущая команда")
        String currentTeam,

        @Schema(description = "Текущий проект")
        ProjectHistoryDto currentProject,

        @Schema(description = "Количество завершенных проектов")
        Integer completedProjectsCount,

        @Schema(description = "Средняя оценка за все проекты")
        BigDecimal averageGrade,

        @Schema(description = "Число команд в которых был")
        Integer teamsCount,

        @Schema(description = "История проектов")
        List<ProjectHistoryDto> projectHistory
) {
}
