package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для отображения проекта в списке")
public record ProjectDto(

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
        long likes,

        @Schema(description = "Количество дизлайков")
        long dislikes,

        @Schema(description = "Количество комментариев")
        long commentsCount,

        @Schema(description = "Голос текущего пользователя (true - лайк, false - дизлайк, null - не голосовал)")
        Boolean userVote
) {
}
