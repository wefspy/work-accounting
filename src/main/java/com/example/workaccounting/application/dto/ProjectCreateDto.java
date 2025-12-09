package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для создания нового проекта")
public record ProjectCreateDto(

        @Schema(description = "Заголовок проекта")
        String title,

        @Schema(description = "Описание проекта")
        String description,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Размер команды")
        Integer teamSize
) {
}
