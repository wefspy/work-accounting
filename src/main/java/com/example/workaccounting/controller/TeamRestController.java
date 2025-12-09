package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.application.service.TeamService;
import com.example.workaccounting.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Команды", description = "API для работы с командами")
public class TeamRestController {

    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Создать команду")
    public ResponseEntity<TeamDto> createTeam(
            @RequestBody TeamCreateDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        Long userId = userDetails.getId();
        TeamDto created = teamService.createTeam(dto, userId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/project")
    @Operation(summary = "Привязать команду к проекту")
    public ResponseEntity<Void> assignProject(@PathVariable Long id,
                                              @RequestBody TeamAssignProjectDto dto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.assignTeamToProject(id, dto.projectId(), userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/grade")
    @Operation(summary = "Оценить работу команды")
    public ResponseEntity<Void> gradeTeam(@PathVariable Long id,
                                          @RequestBody TeamGradeDto dto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.gradeTeam(id, dto, userDetails.getId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Получение подробной информации о команде")
    public ResponseEntity<TeamDetailedDto> getTeamDetails(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamDetails(id));
    }

    @DeleteMapping("/{id}/participants/{participantId}")
    @Operation(summary = "Удалить участника из команды")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long id,
                                                  @PathVariable Long participantId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.removeParticipant(id, participantId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/participants/{participantId}")
    @Operation(summary = "Добавить участника в команду")
    public ResponseEntity<Void> addParticipant(@PathVariable Long id,
                                               @PathVariable Long participantId,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.addParticipant(id, participantId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о команде")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable Long id,
                                              @RequestBody TeamUpdateDto dto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto, userDetails.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить команду")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.deleteTeam(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/grades/{evaluationId}")
    @Operation(summary = "Удалить оценку команды")
    public ResponseEntity<Void> deleteTeamGrade(@PathVariable Long id,
                                                @PathVariable Long evaluationId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        teamService.deleteTeamGrade(id, evaluationId, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
