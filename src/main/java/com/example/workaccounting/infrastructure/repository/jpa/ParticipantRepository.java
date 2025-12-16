package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p WHERE " +
            "LOWER(CONCAT(p.lastName, ' ', p.firstName, ' ', COALESCE(p.middleName, ''))) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Participant> searchByFio(@Param("query") String query, Pageable pageable);
}
