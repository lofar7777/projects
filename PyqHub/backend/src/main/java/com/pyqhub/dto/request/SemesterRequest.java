package com.pyqhub.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SemesterRequest {

    @NotNull(message = "Semester number is required")
    @Min(1)
    @Max(12)
    private Integer number;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
