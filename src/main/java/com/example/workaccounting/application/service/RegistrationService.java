package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.RegisterDto;
import com.example.workaccounting.application.dto.UserProfileInfoDto;
import com.example.workaccounting.application.exception.EmailAlreadyTakenException;
import com.example.workaccounting.application.exception.RoleNotFoundException;
import com.example.workaccounting.application.mapper.UserProfileInfoMapper;
import com.example.workaccounting.domain.model.Role;
import com.example.workaccounting.domain.model.User;
import com.example.workaccounting.domain.model.UserInfo;
import com.example.workaccounting.infrastructure.repository.jpa.RoleRepository;
import com.example.workaccounting.infrastructure.repository.jpa.UserInfoRepository;
import com.example.workaccounting.infrastructure.repository.jpa.UserRepository;
import com.example.workaccounting.infrastructure.security.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final UserProfileInfoMapper userProfileInfoMapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
                               UserInfoRepository userInfoRepository,
                               RoleRepository roleRepository,
                               UserProfileInfoMapper userProfileInfoMapper,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.roleRepository = roleRepository;
        this.userProfileInfoMapper = userProfileInfoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserProfileInfoDto register(RegisterDto dto) {
        validateEmail(dto.email());

        User user = createUser(dto);
        Collection<Role> roles = fetchBasicRolesWithRoleUser();
        assignRolesToUser(user, roles);
        UserInfo userInfo = createUserInfo(user, dto);
        userRepository.save(user);

        return userProfileInfoMapper.toDto(user, userInfo, roles);
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyTakenException("Email %s уже занято".formatted(email));
        }
    }

    private User createUser(RegisterDto dto) {
        return new User(dto.email(), passwordEncoder.encode(dto.password()));
    }

    private UserInfo createUserInfo(User user, RegisterDto dto) {
        return new UserInfo(user, dto);
    }

    private Collection<Role> fetchBasicRolesWithRoleUser() {
        Set<String> basicRoles = Set.of(RoleEnum.USER.name());
        Collection<Role> foundRoles = roleRepository.findByNameIn(basicRoles);

        if (basicRoles.size() != foundRoles.size()) {
            Set<String> foundNames = foundRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            List<String> notFoundNames = basicRoles.stream()
                    .filter(name -> !foundNames.contains(name))
                    .toList();

            throw new RoleNotFoundException(
                    String.format("Роли %s не найдены", notFoundNames)
            );
        }

        return foundRoles;
    }

    private void assignRolesToUser(User user, Collection<Role> roles) {
        user.addRoles(roles);
    }
}
