package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRevisionRepository extends JpaRepository<ProjectRevision, Long> {
    void deleteByProjectId(Long projectId);
}
