package com.example.workaccounting.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "Сведения о пользователе")
public record UserDto(
    @Schema(description = "ID пользователя")
    Long id,
    @Schema(description = "Роли пользователя")
    Set<String> roles,
    @Schema(description = "Имя")
    String firstName,
    @Schema(description = "Фамилия")
    String lastName,
    @Schema(description = "Отчество")
    String middleName,
    @Schema(description = "Email")
    String email
) {}
