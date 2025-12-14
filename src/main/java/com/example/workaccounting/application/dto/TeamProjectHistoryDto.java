package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "История проектов команды")
public record TeamProjectHistoryDto(
        @Schema(description = "ID семестра")
        Long semesterId,

        @Schema(description = "Название семестра")
        String semesterName,

        @Schema(description = "ID проекта")
        Long projectId,

        @Schema(description = "Название проекта")
        String title,

        @Schema(description = "Список наставников")
        List<MentorDto> mentors,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Описание проекта")
        String description,

        @Schema(description = "Средняя оценка за проект")
        Double averageGrade,

        @Schema(description = "Активен сейчас")
        boolean isActive,

        @Schema(description = "Дата назначения")
        LocalDateTime assignedAt,

        @Schema(description = "Дата снятия")
        LocalDateTime unassignedAt
) {}
