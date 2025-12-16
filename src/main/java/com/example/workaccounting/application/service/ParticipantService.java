package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.*;
import com.example.workaccounting.domain.model.Participant;
import com.example.workaccounting.domain.model.ProjectTeam;
import com.example.workaccounting.domain.model.TeamMembership;
import com.example.workaccounting.domain.model.UserInfo;
import com.example.workaccounting.infrastructure.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final UserInfoRepository userInfoRepository;
    private final TeamMembershipRepository teamMembershipRepository;
    private final ProjectTeamRepository projectTeamRepository;
    private final MilestoneEvaluationRepository milestoneEvaluationRepository;

    @Transactional
    public ParticipantDto createParticipant(ParticipantCreateDto dto, Long userId) {
        UserInfo creator = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Participant participant = new Participant();
        participant.setFirstName(dto.firstName());
        participant.setLastName(dto.lastName());
        participant.setMiddleName(dto.middleName());
        participant.setBio(dto.bio());
        participant.setTelegram(dto.telegram());
        participant.setCreatedBy(creator);

        Participant savedParticipant = participantRepository.save(participant);

        return mapToParticipantDto(savedParticipant);
    }

    @Transactional
    public ParticipantDto updateParticipant(Long id, ParticipantUpdateDto dto, Long userId) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        if (dto.firstName() != null) participant.setFirstName(dto.firstName());
        if (dto.lastName() != null) participant.setLastName(dto.lastName());
        if (dto.middleName() != null) participant.setMiddleName(dto.middleName());
        if (dto.bio() != null) participant.setBio(dto.bio());
        if (dto.telegram() != null) participant.setTelegram(dto.telegram());

        Participant savedParticipant = participantRepository.save(participant);
        return mapToParticipantDto(savedParticipant);
    }

    @Transactional
    public void deleteParticipant(Long id, Long userId) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        List<TeamMembership> memberships = teamMembershipRepository.findByParticipantId(id);
        teamMembershipRepository.deleteAll(memberships);

        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public ParticipantDetailedDto getParticipantDetails(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));

        List<TeamMembership> memberships = teamMembershipRepository.findByParticipantId(id);

        int teamsCount = (int) memberships.stream().map(m -> m.getTeam().getId()).distinct().count();

        TeamMembership currentMembership = memberships.stream()
                .filter(m -> m.getLeftAt() == null)
                .findFirst()
                .orElse(null);
        String currentTeamName = currentMembership != null ? currentMembership.getTeam().getName() : null;

        List<Long> teamIds = memberships.stream().map(m -> m.getTeam().getId()).distinct().toList();

        List<ProjectTeam> allProjectTeams = teamIds.isEmpty() ? Collections.emptyList() : projectTeamRepository.findByTeamIdIn(teamIds);

        List<ProjectTeam> participatedProjectTeams = new ArrayList<>();
        for (TeamMembership m : memberships) {
            for (ProjectTeam pt : allProjectTeams) {
                if (pt.getTeam().getId().equals(m.getTeam().getId())) {
                    LocalDateTime joinTime = m.getJoinedAt();
                    LocalDateTime leaveTime = m.getLeftAt() != null ? m.getLeftAt() : LocalDateTime.now();

                    LocalDateTime assignTime = pt.getAssignedAt();
                    LocalDateTime unassignTime = pt.getUnassignedAt() != null ? pt.getUnassignedAt() : LocalDateTime.now();

                    if (joinTime.isBefore(unassignTime) && leaveTime.isAfter(assignTime)) {
                        participatedProjectTeams.add(pt);
                    }
                }
            }
        }
        participatedProjectTeams = participatedProjectTeams.stream().distinct().toList();

        long completedProjectsCount = participatedProjectTeams.stream()
                .filter(pt -> !pt.isActive())
                .count();

        List<Long> participatedProjectTeamIds = participatedProjectTeams.stream().map(ProjectTeam::getId).toList();
        BigDecimal averageGrade = BigDecimal.ZERO;
        if (!participatedProjectTeamIds.isEmpty()) {
            averageGrade = milestoneEvaluationRepository.getAverageScoreByProjectTeamIds(participatedProjectTeamIds);
            if (averageGrade == null) averageGrade = BigDecimal.ZERO;
        }

        List<ProjectHistoryDto> history = participatedProjectTeams.stream()
                .map(pt -> ProjectHistoryDto.builder()
                        .projectId(pt.getProject().getId())
                        .teamId(pt.getTeam().getId())
                        .mentors(pt.getProject().getCurators().stream()
                                .map(curator -> MentorDto.builder()
                                        .id(curator.getId())
                                        .fio(curator.getFullName())
                                        .build())
                                .toList())
                        .projectTitle(pt.getProject().getTitle())
                        .semesterId(pt.getSemester().getId())
                        .semesterName(pt.getSemester().getName())
                        .assignedAt(pt.getAssignedAt())
                        .unassignedAt(pt.getUnassignedAt())
                        .isActive(pt.isActive())
                        .build())
                .sorted((p1, p2) -> p2.assignedAt().compareTo(p1.assignedAt()))
                .toList();

        ProjectHistoryDto currentProjectDto = null;
        if (currentMembership != null) {
             currentProjectDto = history.stream()
                    .filter(h -> h.isActive())
                    .findFirst()
                    .orElse(null);
        }

        if (currentProjectDto != null) {
            ProjectHistoryDto finalCurrentProjectDto = currentProjectDto;
            history = history.stream()
                    .filter(h -> !h.projectId().equals(finalCurrentProjectDto.projectId()))
                    .toList();
        }

        return ParticipantDetailedDto.builder()
                .id(participant.getId())
                .fullname(participant.getFullName())
                .bio(participant.getBio())
                .currentTeamId(currentMembership != null ? currentMembership.getTeam().getId() : null)
                .currentTeam(currentTeamName)
                .currentProject(currentProjectDto)
                .completedProjectsCount((int) completedProjectsCount)
                .averageGrade(averageGrade)
                .teamsCount(teamsCount)
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
    public Page<ParticipantDto> getAllParticipants(String query, Pageable pageable) {
        Page<Participant> page;
        if (query != null && !query.trim().isEmpty()) {
            page = participantRepository.searchByFio(query.trim(), pageable);
        } else {
            page = participantRepository.findAll(pageable);
        }
        return page.map(this::mapToParticipantDto);
    }
}
