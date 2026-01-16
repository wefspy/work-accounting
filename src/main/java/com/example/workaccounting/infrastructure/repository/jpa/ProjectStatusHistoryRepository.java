package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusHistoryRepository extends JpaRepository<ProjectStatusHistory, Long> {
    void deleteByProjectId(Long projectId);
}
