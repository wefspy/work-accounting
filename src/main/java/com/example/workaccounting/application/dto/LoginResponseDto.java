package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Аутентифицированный пользователь")
public record LoginResponseDto(
        @Schema(description = "Токен для доступа к закрытым REST API методам",
                example = "b75c44**********************0dabbb")
        String accessToken,

        @Schema(description = "Токен для обновления accessToken по истечении его срока годности",
                example = "b344bb**********************411e3d")
        String refreshToken
) {
}
