package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Подробная информация о семестре с проектами")
public record SemesterComplexDto(
        @Schema(description = "ID семестра")
        Long id,

        @Schema(description = "Название семестра")
        String name,

        @Schema(description = "Количество проектов")
        int projectCount,

        @Schema(description = "Список проектов")
        List<ProjectSummaryDto> projects,

        @Schema(description = "Активен ли семестр")
        boolean isActive
) {
}
