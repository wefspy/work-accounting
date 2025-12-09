package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {
    java.util.List<ProjectMilestone> findByProjectTeamId(Long projectTeamId);
}
