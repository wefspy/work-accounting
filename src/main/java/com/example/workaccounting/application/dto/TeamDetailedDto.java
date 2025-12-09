package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Подробная информация о команде")
public record TeamDetailedDto(
        @Schema(description = "ID команды")
        Long id,

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Список участников")
        List<TeamMemberShortDto> participants,

        @Schema(description = "Текущий проект")
        TeamProjectHistoryDto currentProject,

        @Schema(description = "История проектов")
        List<TeamProjectHistoryDto> projectHistory
) {}
