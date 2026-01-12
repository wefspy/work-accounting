package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.SemesterComplexDto;
import com.example.workaccounting.application.dto.SemesterCreateDto;
import com.example.workaccounting.application.dto.SemesterDto;
import com.example.workaccounting.application.service.SemesterService;
import com.example.workaccounting.domain.enums.ProjectStatusType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
@Tag(name = "Семестры", description = "API для управления семестрами")
public class SemesterRestController {

    private final SemesterService semesterService;

    public SemesterRestController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @Operation(summary = "Создать новый семестр", description = "Создает новый семестр с указанными датами начала и окончания.")
    @PostMapping
    public ResponseEntity<SemesterDto> createSemester(@RequestBody @Valid SemesterCreateDto dto) {
        SemesterDto createdSemester = semesterService.createSemester(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSemester);
    }

    @Operation(summary = "Обновить семестр", description = "Обновляет данные существующего семестра.")
    @PutMapping("/{id}")
    public ResponseEntity<SemesterDto> updateSemester(@PathVariable Long id, @RequestBody @Valid SemesterCreateDto dto) {
        SemesterDto updatedSemester = semesterService.updateSemester(id, dto);
        return ResponseEntity.ok(updatedSemester);
    }

    @Operation(summary = "Сделать семестр активным", description = "Устанавливает семестр как текущий активный.")
    @PostMapping("/{id}/active")
    public ResponseEntity<Void> makeActive(@PathVariable Long id) {
        semesterService.makeActive(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить семестр", description = "Удаляет семестр по ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить список семестров (кратко)", description = "Возвращает краткий список всех семестров.")
    @ApiResponse(responseCode = "200", description = "Список семестров получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SemesterDto.class))
    })
    @GetMapping
    public ResponseEntity<List<SemesterDto>> getSemesters() {
        return ResponseEntity.ok(semesterService.getAllSemesters());
    }

    @Operation(summary = "Получить семестр с проектами по ID", description = "Возвращает семестр по ID вместе с привязанными проектами.")
    @ApiResponse(responseCode = "200", description = "Семестр с деталями найден", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SemesterComplexDto.class))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SemesterComplexDto> getSemesterById(@PathVariable Long id) {
        return ResponseEntity.ok(semesterService.getSemesterById(id));
    }

    @Operation(summary = "Получить список семестров с деталями (проекты, команды)", description = "Возвращает список семестров с полной детализацией (включая проекты и команды).")
    @ApiResponse(responseCode = "200", description = "Список семестров с деталями успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SemesterComplexDto.class))
    })
    @GetMapping("/details")
    public ResponseEntity<Page<SemesterComplexDto>> getSemestersWithDetails(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) List<ProjectStatusType> statuses,
            @PageableDefault(sort = "endsAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(semesterService.getSemestersWithDetails(query, statuses, pageable));
    }
}
