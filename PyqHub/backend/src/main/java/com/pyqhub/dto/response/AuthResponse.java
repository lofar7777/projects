package com.pyqhub.dto.response;

import com.pyqhub.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String collegeId;
    private String name;
    private String email;
    private Role role;
    private String message;
}
