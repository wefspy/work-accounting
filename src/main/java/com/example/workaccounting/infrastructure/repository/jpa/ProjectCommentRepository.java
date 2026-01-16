package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    long countByProjectId(Long projectId);

    Page<ProjectComment> findByProjectId(Long projectId, Pageable pageable);

    void deleteByProjectId(Long projectId);
}
