package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
