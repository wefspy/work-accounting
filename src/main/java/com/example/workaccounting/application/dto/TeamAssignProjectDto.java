package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO для привязки команды к проекту")
public record TeamAssignProjectDto(

        @Schema(description = "ID проекта")
        Long projectId
) {
}
