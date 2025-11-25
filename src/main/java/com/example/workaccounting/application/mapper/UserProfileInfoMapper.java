package com.example.workaccounting.application.mapper;

import com.example.workaccounting.application.dto.user.UserProfileInfoDto;
import com.example.workaccounting.domain.model.Role;
import com.example.workaccounting.domain.model.User;
import com.example.workaccounting.domain.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserProfileInfoMapper {
    private final RoleMapper roleMapper;

    public UserProfileInfoMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public UserProfileInfoDto toDto(
            User user,
            UserInfo userInfo,
            Collection<Role> roles
    ) {
        return new UserProfileInfoDto(
                user.getId(),
                user.getEmail(),
                user.getCreatedAt(),
                roleMapper.toDtos(roles),
                userInfo.getLastName(),
                userInfo.getFirstName(),
                userInfo.getMiddleName()
        );
    }

    public UserProfileInfoDto toDto(User userWithInfoAndRoles) {
        UserInfo userInfo = userWithInfoAndRoles.getUserInfo();
        Collection<Role> roles = userWithInfoAndRoles.getRoles();

        return toDto(userWithInfoAndRoles, userInfo, roles);
    }
}
