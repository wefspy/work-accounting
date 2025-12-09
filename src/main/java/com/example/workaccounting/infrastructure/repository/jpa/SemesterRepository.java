package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findByName(String name);

    Optional<Semester> findByActiveTrue();

    @Query("SELECT DISTINCT s FROM Semester s JOIN s.projects p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Semester> findDistinctByProjectsTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);
}
