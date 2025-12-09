package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findBySemesterId(Long semesterId);

    List<Project> findBySemesterIdAndTitleContainingIgnoreCase(Long semesterId, String title);

    Page<Project> findByStatusName(String statusName, Pageable pageable);
}
