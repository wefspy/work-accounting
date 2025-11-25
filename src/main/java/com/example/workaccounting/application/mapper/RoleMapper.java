package com.example.workaccounting.application.mapper;

import com.example.workaccounting.application.dto.RoleDto;
import com.example.workaccounting.domain.model.Role;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDto toDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }

    public Collection<RoleDto> toDtos(Collection<Role> roles) {
        return roles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
