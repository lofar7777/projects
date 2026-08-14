package com.pyqhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectRequest {

    @NotBlank(message = "Subject name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 20)
    private String code;

    private String description;

    @NotNull(message = "Semester ID is required")
    private Long semesterId;
}
