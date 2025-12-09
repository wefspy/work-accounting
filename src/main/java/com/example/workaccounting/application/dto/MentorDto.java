package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Информация о наставнике")
public record MentorDto(
        @Schema(description = "ID наставника")
        Long id,

        @Schema(description = "ФИО наставника")
        String fullName
) {
}
