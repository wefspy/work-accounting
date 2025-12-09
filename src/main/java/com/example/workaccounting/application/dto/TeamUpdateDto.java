package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для обновления информации о команде")
public record TeamUpdateDto(

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Описание команды")
        String description
) {
}
