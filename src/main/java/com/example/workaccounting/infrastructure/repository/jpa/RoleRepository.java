package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Collection<Role> findByNameIn(Collection<String> names);
}
