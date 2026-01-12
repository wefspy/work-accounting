package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.UserDto;
import com.example.workaccounting.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получить список всех пользователей", description = "Возвращает список всех зарегистрированных пользователей системы.")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
