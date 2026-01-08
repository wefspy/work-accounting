package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Сводная информация об оценке команды")
public record TeamGradeSummaryDto(

        @Schema(description = "ID проекта")
        Long projectId,

        @Schema(description = "Название проекта")
        String projectTitle,

        @Schema(description = "ID автора оценки")
        Long authorId,

        @Schema(description = "ФИО автора оценки")
        String authorName,

        @Schema(description = "Комментарий")
        String comment,

        @Schema(description = "Оценка")
        BigDecimal score
) {
}
