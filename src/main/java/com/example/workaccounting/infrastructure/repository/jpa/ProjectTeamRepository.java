package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, Long> {
    List<ProjectTeam> findByProjectId(Long projectId);
    
    List<ProjectTeam> findByTeamId(Long teamId);

    Optional<ProjectTeam> findByTeamIdAndActiveTrue(Long teamId);

    List<ProjectTeam> findByTeamIdIn(List<Long> teamIds);

    boolean existsByTeamIdAndProjectId(Long teamId, Long projectId);
}
