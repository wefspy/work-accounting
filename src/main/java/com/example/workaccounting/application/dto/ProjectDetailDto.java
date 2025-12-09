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

        @Schema(description = "Количество лайков")
        long likesCount,

        @Schema(description = "Количество дизлайков")
        long dislikesCount,

        @Schema(description = "Количество комментариев")
        long commentsCount,

        @Schema(description = "ID создателя")
        Long creatorId,

        @Schema(description = "ФИО создателя")
        String creatorFio
) {
}
