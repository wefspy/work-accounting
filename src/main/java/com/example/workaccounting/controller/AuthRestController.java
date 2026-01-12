package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.ApiErrorDto;
import com.example.workaccounting.application.dto.LoginRequestDto;
import com.example.workaccounting.application.dto.LoginResponseDto;
import com.example.workaccounting.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "API для аутентификации пользователей и обновлении токенов")
public class AuthRestController {
    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Аутентификация пользователя (логин)", description = "Позволяет пользователю войти в систему, предоставив учетные данные. Возвращает пару токенов (access и refresh).")
    @ApiResponse(responseCode = "200", description = "Успешная аутентификация", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.status(200).body(loginResponseDto);
    }

    @Operation(summary = "Обновление access/refresh токенов по refresh токену", description = "Позволяет получить новую пару токенов, используя действительный refresh токен.")
    @ApiResponse(responseCode = "200", description = "Токены успешно обновлены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "401", description = "Неверный или просроченный refresh токен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestParam String refreshToken) {
        LoginResponseDto loginResponseDto = authService.refresh(refreshToken);
        return ResponseEntity.status(200).body(loginResponseDto);
    }
}
