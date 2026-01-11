package com.example.workaccounting.controller;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.application.service.ProjectService;
import com.example.workaccounting.infrastructure.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {

    private final ProjectService projectService;

    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Получение списка проектов (только VOTING)")
    @ApiResponse(responseCode = "200", description = "Список проектов успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDto.class))
    })
    @GetMapping
    public ResponseEntity<Page<ProjectDto>> getAllProjects(
            @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(projectService.getAllProjects(pageable, userDetails != null ? userDetails.getId() : null));
    }

    @Operation(summary = "Получение подробной информации о проекте")
    @ApiResponse(responseCode = "200", description = "Информация о проекте успешно получена", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDetailDto.class))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailDto> getProject(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(projectService.getProjectDetails(id, userDetails != null ? userDetails.getId() : null));
    }


    @Operation(summary = "Создать новый проект")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody @Valid ProjectCreateDto dto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectDto createdProject = projectService.createProject(dto, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @Operation(summary = "Обновить существующий проект")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id,
                                                    @RequestBody @Valid ProjectUpdateDto dto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectDto updatedProject = projectService.updateProject(id, dto, userDetails.getId());
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Проголосовать за проект (лайк/дизлайк)")
    @PostMapping("/{id}/votes")
    public void voteProject(@PathVariable Long id, @RequestParam boolean value,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectService.voteProject(id, value, userDetails.getId());
    }

    @Operation(summary = "Получение комментариев к проекту")
    @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDto.class))
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentDto>> getProjectComments(@PathVariable Long id,
                                                               @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(projectService.getProjectComments(id, pageable));
    }

    @Operation(summary = "Добавить комментарий к проекту")
    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable Long id,
                                 @RequestBody @Valid CommentCreateDto dto,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return projectService.addComment(id, dto, userDetails.getId());
    }

    @Operation(summary = "Удалить проект")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        projectService.deleteProject(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить статус проекта")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectDto> updateProjectStatus(@PathVariable Long id,
                                                          @RequestParam String status,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectDto updatedProject = projectService.updateProjectStatus(id, status, userDetails.getId());
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Обновить семестр проекта")
    @PatchMapping("/{id}/semester")
    public ResponseEntity<ProjectDto> updateProjectSemester(@PathVariable Long id,
                                                            @RequestBody @Valid ProjectChangeSemesterDto dto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectDto updatedProject = projectService.updateProjectSemester(id, dto.semesterId(), userDetails.getId());
        return ResponseEntity.ok(updatedProject);
    }

    @Operation(summary = "Обновить список наставников проекта")
    @PutMapping("/{id}/mentors")
    public ResponseEntity<ProjectDto> updateProjectMentors(@PathVariable Long id,
                                                           @RequestBody @Valid ProjectUpdateMentorsDto dto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProjectDto updatedProject = projectService.updateProjectMentors(id, dto, userDetails.getId());
        return ResponseEntity.ok(updatedProject);
    }
}
