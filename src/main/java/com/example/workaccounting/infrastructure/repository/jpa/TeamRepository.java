package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
