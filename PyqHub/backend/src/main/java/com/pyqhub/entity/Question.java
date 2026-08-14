package com.pyqhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @NotNull
    @Column(nullable = false)
    private Integer year;

    @Column
    private Integer marks;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    @Builder.Default
    private QuestionType questionType = QuestionType.SHORT_ANSWER;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    /** Path or URL of the attached image/PDF (null if no attachment) */
    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
