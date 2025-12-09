package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "DTO для обновления списка наставников проекта")
public record ProjectUpdateMentorsDto(
        @Schema(description = "Список ID новых наставников")
        List<Long> mentorIds
) {
}
