package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.domain.enums.EvaluatorType;
import com.example.workaccounting.domain.model.*;
import com.example.workaccounting.infrastructure.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final ParticipantRepository participantRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProjectTeamRepository projectTeamRepository;
    private final ProjectRepository projectRepository;
    private final SemesterRepository semesterRepository;
    private final ProjectMilestoneRepository projectMilestoneRepository;
    private final MilestoneEvaluationRepository milestoneEvaluationRepository;

    @Transactional
    public TeamDto createTeam(TeamCreateDto dto, Long userId) {
        UserInfo creator = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = new Team();
        team.setName(dto.name());
        team.setDescription(dto.description());
        team.setCreatedBy(creator);

        Team savedTeam = teamRepository.save(team);

        List<Long> participantIds = dto.participantIds();
        if (participantIds != null && !participantIds.isEmpty()) {
            List<Participant> participants = participantRepository.findAllById(participantIds);
            
            if (participants.size() != participantIds.size()) {
                throw new RuntimeException("Some participants were not found");
            }

            for (Participant participant : participants) {
                TeamMembership membership = new TeamMembership();
                membership.setTeam(savedTeam);
                membership.setParticipant(participant);
                membership.setRole("MEMBER");
                teamMembershipRepository.save(membership);
            }
        }

        return mapToTeamDto(savedTeam);
    }

    @Transactional
    public void assignTeamToProject(Long teamId, Long projectId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        var activeSemester = semesterRepository.findByActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active semester found"));

        if (projectTeamRepository.existsByTeamIdAndProjectId(teamId, projectId)) {
            throw new RuntimeException("Team has already been assigned to this project");
        }

        projectTeamRepository.findByTeamIdAndActiveTrue(teamId)
                .ifPresent(activeProjectTeam -> {
                    activeProjectTeam.setActive(false);
                    activeProjectTeam.setUnassignedAt(LocalDateTime.now());
                    projectTeamRepository.save(activeProjectTeam);
                });

        ProjectTeam projectTeam = new ProjectTeam();
        projectTeam.setTeam(team);
        projectTeam.setProject(project);
        projectTeam.setSemester(activeSemester);
        projectTeam.setActive(true);
        projectTeamRepository.save(projectTeam);
    }

    @Transactional
    public void gradeTeam(Long teamId, TeamGradeDto dto, Long userId) {
        UserInfo evaluator = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Evaluator user not found"));

        var activeProjectTeam = projectTeamRepository.findByTeamIdAndActiveTrue(teamId)
                .orElseThrow(() -> new RuntimeException("Team does not have an active project"));

        ProjectMilestone milestone = new ProjectMilestone();
        milestone.setProjectTeam(activeProjectTeam);
        milestone.setCreatedBy(evaluator);
        milestone.setTitle(dto.comment() != null ? dto.comment() : "Evaluation");
        milestone.setDescription(dto.feedback());
        milestone.setDueAt(LocalDateTime.now());
        
        var savedMilestone = projectMilestoneRepository.save(milestone);

        MilestoneEvaluation evaluation = new MilestoneEvaluation();
        evaluation.setProjectMilestone(savedMilestone);
        evaluation.setEvaluatorType(EvaluatorType.MENTOR);
        evaluation.setEvaluatorUser(evaluator);
        evaluation.setExternalContact("");
        evaluation.setScore(dto.score());
        evaluation.setFeedback(dto.feedback());
        
        milestoneEvaluationRepository.save(evaluation);
    }

    @Transactional(readOnly = true)
    public TeamDto mapToTeamDto(Team team) {
        List<TeamMembership> memberships = teamMembershipRepository.findByTeamIdAndLeftAtIsNull(team.getId());
        List<ParticipantDto> participantDtos = memberships.stream()
                .map(m -> mapToParticipantDto(m.getParticipant()))
                .collect(Collectors.toList());

        var projectTeams = projectTeamRepository.findByTeamId(team.getId());
        
        var currentProject = projectTeams.stream()
                .filter(ProjectTeam::isActive)
                .findFirst();

        var history = projectTeams.stream()
                .map(pt -> ProjectHistoryDto.builder()
                        .projectId(pt.getProject().getId())
                        .projectTitle(pt.getProject().getTitle())
                        .semesterId(pt.getSemester().getId())
                        .semesterName(pt.getSemester().getName())
                        .assignedAt(pt.getAssignedAt())
                        .unassignedAt(pt.getUnassignedAt())
                        .isActive(pt.isActive())
                        .build())
                .collect(Collectors.toList());

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdById(team.getCreatedBy().getId())
                .createdByName(team.getCreatedBy().getFullName())
                .createdAt(team.getCreatedAt())
                .participants(participantDtos)
                .currentProjectId(currentProject.map(pt -> pt.getProject().getId()).orElse(null))
                .currentProjectTitle(currentProject.map(pt -> pt.getProject().getTitle()).orElse(null))
                .projectHistory(history)
                .build();
    }

    private ParticipantDto mapToParticipantDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getId())
                .firstName(participant.getFirstName())
                .lastName(participant.getLastName())
                .middleName(participant.getMiddleName())
                .bio(participant.getBio())
                .telegram(participant.getTelegram())
                .createdById(participant.getCreatedBy().getId())
                .createdByName(participant.getCreatedBy().getFullName())
                .build();
    }
    @Transactional(readOnly = true)
    public TeamDetailedDto getTeamDetails(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return mapToTeamDetailedDto(team);
    }

    private TeamDetailedDto mapToTeamDetailedDto(Team team) {
        List<TeamMembership> memberships = teamMembershipRepository.findByTeamIdAndLeftAtIsNull(team.getId());
        List<TeamMemberShortDto> participants = memberships.stream()
                .map(m -> TeamMemberShortDto.builder()
                        .id(m.getParticipant().getId())
                        .fio(m.getParticipant().getFullName())
                        .build())
                .collect(Collectors.toList());

        List<ProjectTeam> projectTeams = projectTeamRepository.findByTeamId(team.getId());

        var currentProjectTeam = projectTeams.stream()
                .filter(ProjectTeam::isActive)
                .findFirst()
                .orElse(null);

        TeamProjectHistoryDto currentProjectDto = null;
        if (currentProjectTeam != null) {
            currentProjectDto = mapToTeamProjectHistoryDto(currentProjectTeam);
        }

        List<TeamProjectHistoryDto> projectHistory = projectTeams.stream()
                .filter(pt -> !pt.isActive())
                .map(this::mapToTeamProjectHistoryDto)
                .collect(Collectors.toList());

        return TeamDetailedDto.builder()
                .id(team.getId())
                .name(team.getName())
                .participants(participants)
                .currentProject(currentProjectDto)
                .projectHistory(projectHistory)
                .build();
    }

    private TeamProjectHistoryDto mapToTeamProjectHistoryDto(ProjectTeam pt) {
        Project project = pt.getProject();

        List<ProjectMilestone> milestones = projectMilestoneRepository.findByProjectTeamId(pt.getId());
        
        List<Double> scores = milestones.stream()
                .flatMap(m -> milestoneEvaluationRepository.findByProjectMilestoneId(m.getId()).stream())
                .map(e -> e.getScore().doubleValue())
                .collect(Collectors.toList());

        Double averageGrade = null;
        if (!scores.isEmpty()) {
            averageGrade = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        return TeamProjectHistoryDto.builder()
                .semesterId(pt.getSemester().getId())
                .semesterName(pt.getSemester().getName())
                .projectId(project.getId())
                .title(project.getTitle())
                .mentors(project.getCurators().stream()
                        .map(curator -> MentorDto.builder()
                                .id(curator.getId())
                                .fio(curator.getFullName())
                                .build())
                        .collect(Collectors.toList()))
                .techStack(project.getTechStack())
                .description(project.getDescription())
                .averageGrade(averageGrade)
                .isActive(pt.isActive())
                .assignedAt(pt.getAssignedAt())
                .unassignedAt(pt.getUnassignedAt())
                .build();
    }

    @Transactional
    public void removeParticipant(Long teamId, Long participantId, Long userId) {
        TeamMembership membership = teamMembershipRepository.findByTeamIdAndParticipantIdAndLeftAtIsNull(teamId, participantId)
                .orElseThrow(() -> new RuntimeException("Active membership not found"));

        membership.setLeftAt(LocalDateTime.now());
        teamMembershipRepository.save(membership);
    }

    @Transactional
    public void addParticipant(Long teamId, Long participantId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        teamMembershipRepository.findByTeamIdAndParticipantIdAndLeftAtIsNull(teamId, participantId)
                .ifPresent(m -> {
                    throw new RuntimeException("Participant is already a member of this team");
                });

        TeamMembership membership = new TeamMembership();
        membership.setTeam(team);
        membership.setParticipant(participant);
        membership.setRole("MEMBER");
        teamMembershipRepository.save(membership);
    }

    @Transactional
    public TeamDto updateTeam(Long teamId, TeamUpdateDto dto, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (dto.name() != null) {
            team.setName(dto.name());
        }
        if (dto.description() != null) {
            team.setDescription(dto.description());
        }

        Team savedTeam = teamRepository.save(team);
        return mapToTeamDto(savedTeam);
    }

    @Transactional
    public void deleteTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<TeamMembership> memberships = teamMembershipRepository.findByTeamId(teamId);
        teamMembershipRepository.deleteAll(memberships);

        List<ProjectTeam> projectTeams = projectTeamRepository.findByTeamId(teamId);
        projectTeamRepository.deleteAll(projectTeams);

        teamRepository.delete(team);
    }

    @Transactional
    public void deleteTeamGrade(Long teamId, Long evaluationId, Long userId) {
        MilestoneEvaluation evaluation = milestoneEvaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        if (!evaluation.getProjectMilestone().getProjectTeam().getTeam().getId().equals(teamId)) {
            throw new RuntimeException("Evaluation does not belong to this team");
        }

        ProjectMilestone milestone = evaluation.getProjectMilestone();
        
        milestoneEvaluationRepository.delete(evaluation);
        projectMilestoneRepository.delete(milestone);
    }

    @Transactional(readOnly = true)
    public Page<TeamDetailedDto> getAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable)
                .map(this::mapToTeamDetailedDto);
    }

    @Transactional(readOnly = true)
    public List<TeamGradeSummaryDto> getTeamGrades(Long teamId) {
        List<MilestoneEvaluation> evaluations = milestoneEvaluationRepository
                .findByProjectMilestone_ProjectTeam_TeamId(teamId);
        return evaluations.stream()
                .map(this::mapToTeamGradeSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TeamGradeSummaryDto> getTeamProjectGrades(Long teamId, Long projectId) {
        List<MilestoneEvaluation> evaluations = milestoneEvaluationRepository
                .findByProjectMilestone_ProjectTeam_TeamIdAndProjectMilestone_ProjectTeam_ProjectId(teamId, projectId);
        return evaluations.stream()
                .map(this::mapToTeamGradeSummaryDto)
                .collect(Collectors.toList());
    }

    private TeamGradeSummaryDto mapToTeamGradeSummaryDto(MilestoneEvaluation evaluation) {
        return TeamGradeSummaryDto.builder()
                .id(evaluation.getId())
                .projectId(evaluation.getProjectMilestone().getProjectTeam().getProject().getId())
                .projectTitle(evaluation.getProjectMilestone().getProjectTeam().getProject().getTitle())
                .authorId(evaluation.getEvaluatorUser().getId())
                .authorName(evaluation.getEvaluatorUser().getFullName())
                .comment(evaluation.getFeedback())
                .score(evaluation.getScore())
                .build();
    }


}

