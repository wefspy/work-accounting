package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "DTO для создания команды")
public record TeamCreateDto(

        @Schema(description = "Название команды")
        String name,

        @Schema(description = "Описание команды")
        String description,

        @Schema(description = "Список ID участников")
        List<Long> participantIds
) {
}
