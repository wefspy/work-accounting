package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.ProjectVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectVoteRepository extends JpaRepository<ProjectVote, Long> {
    long countByProjectIdAndValue(Long projectId, boolean value);

    Optional<ProjectVote> findByProjectIdAndVoterId(Long projectId, Long userId);

    void deleteByProjectId(Long projectId);
}
