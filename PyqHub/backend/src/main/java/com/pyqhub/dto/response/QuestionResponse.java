package com.pyqhub.dto.response;

import com.pyqhub.entity.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private String questionText;
    private Integer year;
    private Integer marks;
    private QuestionType questionType;
    private String explanation;
    private String fileUrl;

    // Hierarchy breadcrumbs
    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;
    private Long semesterId;
    private Integer semesterNumber;
    private Long courseId;
    private String courseName;
    private String courseCode;

    // Metadata
    private String addedByCollegeId;
    private String addedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
