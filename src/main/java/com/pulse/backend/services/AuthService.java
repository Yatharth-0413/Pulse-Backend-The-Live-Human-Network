package com.pulse.backend.services;

import com.pulse.backend.dto.AuthResponse;
import com.pulse.backend.dto.LoginRequest;
import com.pulse.backend.dto.RegisterRequest;
import com.pulse.backend.entity.User;
import com.pulse.backend.exceptions.BadRequestException;
import com.pulse.backend.exceptions.UnauthorizedException;
import com.pulse.backend.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder
            passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(
            UserRepository userRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse register(
            RegisterRequest request) {

        if (userRepository
                .findByUsername(
                        request.getUsername()
                )
                .isPresent()) {

            throw new BadRequestException("Username already exists");
        }

        User user = new User();

        user.setUsername(
                request.getUsername().trim()
                        .toLowerCase());

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        user.setJoinedAt(LocalDateTime.now());
        userRepository.save(user);

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return new AuthResponse(
                token,
                user.getUsername()
        );
    }

    public AuthResponse login(
            LoginRequest request) {

        User user =
                userRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        boolean valid =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );

        if (!valid) {

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return new AuthResponse(
                token,
                user.getUsername()
        );
    }
}