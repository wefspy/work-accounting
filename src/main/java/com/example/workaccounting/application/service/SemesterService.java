package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.domain.model.Project;
import com.example.workaccounting.domain.model.ProjectTeam;
import com.example.workaccounting.domain.model.Semester;
import com.example.workaccounting.domain.model.UserInfo;
import com.example.workaccounting.infrastructure.repository.jpa.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SemesterService {

    private final ProjectRepository projectRepository;
    private final ProjectTeamRepository projectTeamRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final MilestoneEvaluationRepository milestoneEvaluationRepository;
    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository,
                           ProjectRepository projectRepository,
                           ProjectTeamRepository projectTeamRepository,
                           TeamMembershipRepository teamMembershipRepository,
                           MilestoneEvaluationRepository milestoneEvaluationRepository) {
        this.semesterRepository = semesterRepository;
        this.projectRepository = projectRepository;
        this.projectTeamRepository = projectTeamRepository;
        this.teamMembershipRepository = teamMembershipRepository;
        this.milestoneEvaluationRepository = milestoneEvaluationRepository;
    }

    @Transactional
    public SemesterDto createSemester(SemesterCreateDto dto) {
        if (semesterRepository.findByName(dto.name()).isPresent()) {
            throw new RuntimeException("Semester with name " + dto.name() + " already exists");
        }

        Semester semester = new Semester();
        semester.setName(dto.name());
        semester.setStartsAt(dto.startsAt());
        semester.setEndsAt(dto.endsAt());

        Semester savedSemester = semesterRepository.save(semester);

        return SemesterDto.builder()
                .id(savedSemester.getId())
                .name(savedSemester.getName())
                .startsAt(savedSemester.getStartsAt())
                .endsAt(savedSemester.getEndsAt())
                .isActive(savedSemester.isActive())
                .build();
    }

    @Transactional
    public SemesterDto updateSemester(Long id, SemesterCreateDto dto) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        semester.setName(dto.name());
        semester.setStartsAt(dto.startsAt());
        semester.setEndsAt(dto.endsAt());

        Semester savedSemester = semesterRepository.save(semester);

        return SemesterDto.builder()
                .id(savedSemester.getId())
                .name(savedSemester.getName())
                .startsAt(savedSemester.getStartsAt())
                .endsAt(savedSemester.getEndsAt())
                .isActive(savedSemester.isActive())
                .build();
    }

    @Transactional
    public void makeActive(Long id) {
        Semester newActive = semesterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        semesterRepository.findByActiveTrue()
                .ifPresent(currentActive -> {
                    if (!currentActive.getId().equals(newActive.getId())) {
                        currentActive.setActive(false);
                        semesterRepository.save(currentActive);
                    }
                });

        newActive.setActive(true);
        semesterRepository.save(newActive);
    }

    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new RuntimeException("Semester not found");
        }
        semesterRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SemesterComplexDto> getSemestersWithDetails(Pageable pageable) {
        return semesterRepository.findAll(pageable)
                .map(semester -> {
                    List<Project> projects = projectRepository.findBySemesterId(semester.getId());
                    List<ProjectSummaryDto> projectDtos = projects.stream().map(project -> {
                        List<String> curators = project.getCurators().stream()
                                .map(UserInfo::getFullName)
                                .toList();

                        List<ProjectTeam> projectTeams = projectTeamRepository.findByProjectId(project.getId());
                        List<TeamSummaryDto> teamDtos = projectTeams.stream().map(pt -> {
                            var team = pt.getTeam();
                            var members = teamMembershipRepository.findByTeamId(team.getId()).stream()
                                    .map(tm -> tm.getParticipant().getLastName() + " " + tm.getParticipant().getFirstName())
                                    .toList();

                            BigDecimal avgRating = milestoneEvaluationRepository.getAverageScoreByTeamId(team.getId());

                            return TeamSummaryDto.builder()
                                    .name(team.getName())
                                    .averageRating(avgRating != null ? avgRating : BigDecimal.ZERO)
                                    .members(members)
                                    .build();
                        }).toList();

                        return ProjectSummaryDto.builder()
                                .title(project.getTitle())
                                .status(project.getStatus().getName())
                                .techStack(project.getTechStack())
                                .curators(curators)
                                .teams(teamDtos)
                                .build();
                    }).toList();

                    return SemesterComplexDto.builder()
                            .id(semester.getId())
                            .name(semester.getName())
                            .projectCount(projects.size())
                            .projects(projectDtos)
                            .isActive(semester.isActive())
                            .build();
                });
    }

    @Transactional(readOnly = true)
    public Page<SemesterComplexDto> searchProjects(String title, Pageable pageable) {
        return semesterRepository.findDistinctByProjectsTitleContainingIgnoreCase(title, pageable)
                .map(semester -> {
                    List<Project> projects = projectRepository.findBySemesterIdAndTitleContainingIgnoreCase(semester.getId(), title);

                    List<ProjectSummaryDto> projectDtos = projects.stream().map(project -> {
                        List<String> curators = project.getCurators().stream()
                                .map(UserInfo::getFullName)
                                .toList();

                        List<ProjectTeam> projectTeams = projectTeamRepository.findByProjectId(project.getId());
                        List<TeamSummaryDto> teamDtos = projectTeams.stream().map(pt -> {
                            var team = pt.getTeam();
                            var members = teamMembershipRepository.findByTeamId(team.getId()).stream()
                                    .map(tm -> tm.getParticipant().getLastName() + " " + tm.getParticipant().getFirstName())
                                    .toList();

                            BigDecimal avgRating = milestoneEvaluationRepository.getAverageScoreByTeamId(team.getId());

                            return TeamSummaryDto.builder()
                                    .name(team.getName())
                                    .averageRating(avgRating != null ? avgRating : BigDecimal.ZERO)
                                    .members(members)
                                    .build();
                        }).toList();

                        return ProjectSummaryDto.builder()
                                .title(project.getTitle())
                                .status(project.getStatus().getName())
                                .techStack(project.getTechStack())
                                .curators(curators)
                                .teams(teamDtos)
                                .build();
                    }).toList();

                    return SemesterComplexDto.builder()
                            .id(semester.getId())
                            .name(semester.getName())
                            .projectCount(projects.size())
                            .projects(projectDtos)
                            .isActive(semester.isActive())
                            .build();
                });
    }
}
