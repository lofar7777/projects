package com.pyqhub.controller;

import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.dto.response.UserResponse;
import com.pyqhub.entity.Role;
import com.pyqhub.entity.User;
import com.pyqhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** Any authenticated user can get their own profile */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(
                ApiResponse.success(userService.toResponse(currentUser)));
    }

    /** Admin: list all users (paginated) */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageable)));
    }

    /** Admin: get single user by college ID */
    @GetMapping("/{collegeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByCollegeId(
            @PathVariable String collegeId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserByCollegeId(collegeId)));
    }

    /** Admin: promote/demote a user's role */
    @PutMapping("/{collegeId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable String collegeId,
            @RequestParam Role role) {
        return ResponseEntity.ok(
                ApiResponse.success("Role updated successfully",
                        userService.updateUserRole(collegeId, role)));
    }

    /** Admin: approve or deactivate an account */
    @PutMapping("/{collegeId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> toggleApproval(
            @PathVariable String collegeId,
            @RequestParam boolean approved) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        approved ? "Account approved" : "Account deactivated",
                        userService.toggleApproval(collegeId, approved)));
    }
}
