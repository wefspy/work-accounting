package com.example.workaccounting.application.service;

import com.example.workaccounting.application.dto.LoginRequestDto;
import com.example.workaccounting.application.dto.LoginResponseDto;
import com.example.workaccounting.application.dto.UserDto;
import com.example.workaccounting.application.exception.UserNotFoundException;
import com.example.workaccounting.application.mapper.AccessTokenInputMapper;
import com.example.workaccounting.domain.model.Role;
import com.example.workaccounting.domain.model.User;
import com.example.workaccounting.infrastructure.repository.jpa.UserRepository;
import com.example.workaccounting.infrastructure.security.UserDetailsImpl;
import com.example.workaccounting.infrastructure.security.jwt.JwtTokenFactory;
import com.example.workaccounting.infrastructure.security.jwt.dto.AccessTokenInput;
import com.example.workaccounting.infrastructure.security.jwt.parser.RefreshTokenParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenFactory jwtTokenFactory;
    private final RefreshTokenParser refreshTokenParser;
    private final UserRepository userRepository;
    private final AccessTokenInputMapper accessTokenInputMapper;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenFactory jwtTokenFactory,
                       RefreshTokenParser refreshTokenParser,
                       UserRepository userRepository,
                       AccessTokenInputMapper accessTokenInputMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenFactory = jwtTokenFactory;
        this.refreshTokenParser = refreshTokenParser;
        this.userRepository = userRepository;
        this.accessTokenInputMapper = accessTokenInputMapper;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(userDetails);

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userDetails.getId()));

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId()),
                mapToUserDto(user)
        );
    }

    public LoginResponseDto refresh(String refreshToken) {
        Long userId = refreshTokenParser.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("В токен зашит id %s пользователя, которого не существует", userId))
                );

        AccessTokenInput accessTokenInput = accessTokenInputMapper.from(user);

        return new LoginResponseDto(
                jwtTokenFactory.generateAccessToken(accessTokenInput),
                jwtTokenFactory.generateRefreshToken(accessTokenInput.userId()),
                mapToUserDto(user)
        );
    }

    private UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getUserInfo().getFirstName(),
                user.getUserInfo().getLastName(),
                user.getUserInfo().getMiddleName(),
                user.getEmail()
        );
    }
}
