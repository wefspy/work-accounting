package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.ApiErrorDto;
import com.example.workaccounting.application.dto.RegisterDto;
import com.example.workaccounting.application.dto.UserProfileInfoDto;
import com.example.workaccounting.application.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
public class RegistrationRestController {
    private final RegistrationService registrationService;

    public RegistrationRestController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь зарегистрирован", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileInfoDto.class))
    })
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "404", description = "Роли с указанными id не найдены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "409", description = "Указанный username уже занят", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorDto.class))
    })
    @PostMapping
    public ResponseEntity<UserProfileInfoDto> createUserWithInfo(@Valid @RequestBody RegisterDto registerDto) {
        UserProfileInfoDto userProfileInfoDto = registrationService.register(registerDto);
        return ResponseEntity.status(200).body(userProfileInfoDto);
    }
}
