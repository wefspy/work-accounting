package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Schema(description = "Краткая информация о команде")
public record TeamSummaryDto(
        @Schema(description = "ID команды")
        Long id,

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Средняя оценка команды")
        BigDecimal averageRating,

        @Schema(description = "Список участников (ФИО)")
        List<String> members
) {
}
