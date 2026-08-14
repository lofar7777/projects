package com.pyqhub.dto.request;

import com.pyqhub.entity.QuestionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequest {

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Exam year is required")
    @Min(value = 1990, message = "Year must be 1990 or later")
    @Max(value = 2100, message = "Year seems invalid")
    private Integer year;

    private Integer marks;

    private QuestionType questionType;

    private String explanation;

    /** Optional: topic ID under which the question falls */
    @NotNull(message = "Topic ID is required")
    private Long topicId;

    /** Optional file URL — set by backend after upload */
    private String fileUrl;
}
