package com.pyqhub.service;

import com.pyqhub.dto.response.UserResponse;
import com.pyqhub.entity.Role;
import com.pyqhub.entity.User;
import com.pyqhub.exception.BadRequestException;
import com.pyqhub.exception.ResourceNotFoundException;
import com.pyqhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toResponse);
    }

    public UserResponse getUserByCollegeId(String collegeId) {
        User user = userRepository.findByCollegeId(collegeId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUserRole(String collegeId, Role role) {
        User user = userRepository.findByCollegeId(collegeId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));
        user.setRole(role);
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse toggleApproval(String collegeId, boolean approved) {
        User user = userRepository.findByCollegeId(collegeId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("User", collegeId));
        user.setApproved(approved);
        return toResponse(userRepository.save(user));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .collegeId(user.getCollegeId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .approved(user.isApproved())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
