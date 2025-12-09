package com.example.workaccounting.application.dto;

import java.time.LocalDateTime;
import java.util.Collection;

public record UserProfileInfoDto(
        Long id,
        String email,
        LocalDateTime createdAt,
        Collection<RoleDto> roles,
        String lastName,
        String firstName,
        String middleName
) {
}
