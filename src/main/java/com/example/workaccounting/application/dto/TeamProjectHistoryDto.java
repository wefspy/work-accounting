package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "История проектов команды")
public record TeamProjectHistoryDto(
        @Schema(description = "Название семестра")
        String semesterName,

        @Schema(description = "Название проекта")
        String title,

        @Schema(description = "Список наставников")
        List<String> mentors,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Описание проекта")
        String description,

        @Schema(description = "Средняя оценка за проект")
        Double averageGrade
) {}
