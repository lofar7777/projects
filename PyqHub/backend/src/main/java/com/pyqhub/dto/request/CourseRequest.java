package com.pyqhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank(message = "Course name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Course code is required")
    @Size(max = 20)
    private String code;

    private String description;
}
