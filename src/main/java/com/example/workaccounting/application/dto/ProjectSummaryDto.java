package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Краткая информация о проекте в семестре")
public record ProjectSummaryDto(
        @Schema(description = "ID проекта")
        Long id,

        @Schema(description = "Название проекта")
        String title,

        @Schema(description = "Статус проекта")
        String status,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Список кураторов (ФИО)")
        List<String> curators,

        @Schema(description = "Список команд проекта")
        List<TeamSummaryDto> teams
) {
}
