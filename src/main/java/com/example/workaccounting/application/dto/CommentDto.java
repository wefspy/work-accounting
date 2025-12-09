package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "DTO комментария")
public record CommentDto(
        @Schema(description = "Идентификатор комментария")
        Long id,

        @Schema(description = "Текст комментария")
        String body,

        @Schema(description = "Идентификатор автора")
        Long authorId,

        @Schema(description = "Имя автора")
        String authorName,

        @Schema(description = "Дата и время создания")
        LocalDateTime createdAt
) {
}
