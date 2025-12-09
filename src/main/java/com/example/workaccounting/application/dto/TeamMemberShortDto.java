package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Краткая информация об участнике команды")
public record TeamMemberShortDto(
        @Schema(description = "ID участника")
        Long id,

        @Schema(description = "ФИО участника")
        String fio
) {}
