package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "DTO для оценки команды")
public record TeamGradeDto(

        @Schema(description = "Оценка")
        BigDecimal score,

        @Schema(description = "Отзыв")
        String feedback,

        @Schema(description = "Комментарий/Заголовок (например, название этапа)")
        String comment
) {
}
