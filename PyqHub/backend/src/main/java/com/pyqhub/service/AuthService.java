package com.pyqhub.service;

import com.pyqhub.dto.request.LoginRequest;
import com.pyqhub.dto.request.RegisterRequest;
import com.pyqhub.dto.response.AuthResponse;
import com.pyqhub.entity.Role;
import com.pyqhub.entity.User;
import com.pyqhub.exception.BadRequestException;
import com.pyqhub.repository.UserRepository;
import com.pyqhub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByCollegeId(request.getCollegeId())) {
            throw new BadRequestException("College ID '" + request.getCollegeId() + "' is already registered");
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' is already in use");
        }

        User user = User.builder()
                .collegeId(request.getCollegeId().toUpperCase())
                .name(request.getName().trim())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .approved(true)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .collegeId(user.getCollegeId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCollegeId().toUpperCase(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByCollegeId(request.getCollegeId().toUpperCase())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .collegeId(user.getCollegeId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }
}
