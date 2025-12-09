package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "DTO для обновления проекта")
public record ProjectUpdateDto(
        @NotBlank
        @Schema(description = "Название проекта")
        String title,

        @Schema(description = "Описание проекта")
        String description,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Размер команды")
        Integer teamSize
) {
}
