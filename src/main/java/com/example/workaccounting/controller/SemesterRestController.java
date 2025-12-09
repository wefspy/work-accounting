package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.SemesterComplexDto;
import com.example.workaccounting.application.dto.SemesterCreateDto;
import com.example.workaccounting.application.dto.SemesterDto;
import com.example.workaccounting.application.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/semesters")
public class SemesterRestController {

    private final SemesterService semesterService;

    public SemesterRestController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @Operation(summary = "Создать новый семестр")
    @PostMapping
    public ResponseEntity<SemesterDto> createSemester(@RequestBody @Valid SemesterCreateDto dto) {
        SemesterDto createdSemester = semesterService.createSemester(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSemester);
    }

    @Operation(summary = "Обновить семестр")
    @PutMapping("/{id}")
    public ResponseEntity<SemesterDto> updateSemester(@PathVariable Long id, @RequestBody @Valid SemesterCreateDto dto) {
        SemesterDto updatedSemester = semesterService.updateSemester(id, dto);
        return ResponseEntity.ok(updatedSemester);
    }

    @Operation(summary = "Сделать семестр активным")
    @PostMapping("/{id}/active")
    public ResponseEntity<Void> makeActive(@PathVariable Long id) {
        semesterService.makeActive(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить семестр")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить список семестров с деталями (проекты, команды)")
    @ApiResponse(responseCode = "200", description = "Список семестров с деталями успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SemesterComplexDto.class))
    })
    @GetMapping("/details")
    public ResponseEntity<Page<SemesterComplexDto>> getSemestersWithDetails(
            @PageableDefault(sort = "endsAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(semesterService.getSemestersWithDetails(pageable));
    }
    @Operation(summary = "Поиск проектов по названию (возвращает иерархию семестров)")
    @ApiResponse(responseCode = "200", description = "Результаты поиска успешно получены", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SemesterComplexDto.class))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<SemesterComplexDto>> searchProjects(
            @RequestParam String query,
            @PageableDefault(sort = "endsAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(semesterService.searchProjects(query, pageable));
    }
}
