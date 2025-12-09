package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.TeamMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {
    List<TeamMembership> findByTeamId(Long teamId);

    java.util.Optional<TeamMembership> findByTeamIdAndParticipantIdAndLeftAtIsNull(Long teamId, Long participantId);

    List<TeamMembership> findByTeamIdAndLeftAtIsNull(Long teamId);

    List<TeamMembership> findByParticipantId(Long participantId);
}
