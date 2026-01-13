package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Подробная информация о проекте")
public record ProjectDetailDto(
        @Schema(description = "ID проекта")
        Long id,

        @Schema(description = "Заголовок проекта")
        String title,

        @Schema(description = "Описание проекта")
        String description,

        @Schema(description = "Стек технологий")
        String techStack,

        @Schema(description = "Статус проекта")
        String status,

        @Schema(description = "ID семестра")
        Long semesterId,

        @Schema(description = "Название семестра")
        String semesterName,

        @Schema(description = "Количество лайков")
        long likesCount,

        @Schema(description = "Количество дизлайков")
        long dislikesCount,

        @Schema(description = "Количество комментариев")
        long commentsCount,

        @Schema(description = "ID создателя")
        Long creatorId,

        @Schema(description = "ФИО создателя")
        String creatorFio,

        @Schema(description = "Список наставников")
        java.util.List<MentorDto> mentors,

        @Schema(description = "Голос текущего пользователя (true - лайк, false - дизлайк, null - не голосовал)")
        Boolean userVote,

        @Schema(description = "Необходимое число участников")
        Integer teamSize,

        @Schema(description = "Список команд")
        java.util.List<ProjectTeamDto> teams
) {
}
