package com.pyqhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicRequest {

    @NotBlank(message = "Topic name is required")
    @Size(max = 150)
    private String name;

    private String description;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
}
