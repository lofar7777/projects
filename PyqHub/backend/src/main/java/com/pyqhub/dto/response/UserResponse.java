package com.pyqhub.dto.response;

import com.pyqhub.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String collegeId;
    private String name;
    private String email;
    private Role role;
    private boolean approved;
    private LocalDateTime createdAt;
}
