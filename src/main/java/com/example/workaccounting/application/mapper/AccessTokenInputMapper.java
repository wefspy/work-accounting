package com.example.workaccounting.application.mapper;

import com.example.workaccounting.domain.model.Role;
import com.example.workaccounting.domain.model.User;
import com.example.workaccounting.infrastructure.security.UserDetailsImpl;
import com.example.workaccounting.infrastructure.security.jwt.dto.AccessTokenInput;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccessTokenInputMapper {
    public AccessTokenInput from(UserDetailsImpl user) {
        return new AccessTokenInput(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }

    public AccessTokenInput from(User userWithRoles) {
        return new AccessTokenInput(
                userWithRoles.getId(),
                userWithRoles.getEmail(),
                userWithRoles.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}
