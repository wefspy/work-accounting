package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию")
public record LoginRequestDto(
        @Schema(description = "Почта пользователя", example = "string@gmail.com")
        @NotBlank(message = "Username обязательное поле")
        String email,

        @Schema(description = "Пароль", example = "string")
        @NotBlank(message = "Пароль не может быть пустыми")
        String password
) {
}
