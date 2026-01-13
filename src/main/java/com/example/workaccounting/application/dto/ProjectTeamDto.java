package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Информация о команде в проекте")
public record ProjectTeamDto(
        @Schema(description = "ID команды")
        Long id,

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Активна ли команда на проекте")
        boolean active,

        @Schema(description = "Дата назначения")
        LocalDateTime assignedAt,

        @Schema(description = "Дата снятия с проекта")
        LocalDateTime unassignedAt,

        @Schema(description = "Список участников команды")
        java.util.List<TeamMemberShortDto> participants,

        @Schema(description = "Средняя оценка за проект")
        java.math.BigDecimal averageRating
) {
}
