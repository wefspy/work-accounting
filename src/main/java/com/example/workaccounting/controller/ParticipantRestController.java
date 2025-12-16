package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.ParticipantCreateDto;
import com.example.workaccounting.application.dto.ParticipantDetailedDto;
import com.example.workaccounting.application.dto.ParticipantDto;
import com.example.workaccounting.application.dto.ParticipantUpdateDto;
import com.example.workaccounting.application.service.ParticipantService;
import com.example.workaccounting.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Tag(name = "Студенты")
public class ParticipantRestController {

    private final ParticipantService participantService;

    @PostMapping
    @Operation(summary = "Создать участника")
    public ResponseEntity<ParticipantDto> createParticipant(
            @RequestBody ParticipantCreateDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        Long userId = userDetails.getId();
        ParticipantDto created = participantService.createParticipant(dto, userId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить участника")
    public ResponseEntity<ParticipantDto> updateParticipant(
            @PathVariable Long id,
            @RequestBody ParticipantUpdateDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        return ResponseEntity.ok(participantService.updateParticipant(id, dto, userDetails.getId()));
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Получить подробную информацию об участнике")
    public ResponseEntity<ParticipantDetailedDto> getParticipantDetails(
            @PathVariable Long id) {
        return ResponseEntity.ok(participantService.getParticipantDetails(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить участника")
    public ResponseEntity<Void> deleteParticipant(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        participantService.deleteParticipant(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Получить список участников")
    public ResponseEntity<Page<ParticipantDto>> getParticipants(
            @RequestParam(required = false) String query,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(participantService.getAllParticipants(query, pageable));
    }
}
