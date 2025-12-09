package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для создания нового участника")
public record ParticipantCreateDto(

        @Schema(description = "Имя")
        String firstName,

        @Schema(description = "Фамилия")
        String lastName,

        @Schema(description = "Отчество")
        String middleName,

        @Schema(description = "Биография")
        String bio,

        @Schema(description = "Телеграм")
        String telegram
) {
}
