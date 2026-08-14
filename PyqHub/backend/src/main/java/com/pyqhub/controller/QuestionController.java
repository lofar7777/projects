package com.pyqhub.controller;

import com.pyqhub.dto.request.QuestionRequest;
import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.dto.response.QuestionResponse;
import com.pyqhub.entity.QuestionType;
import com.pyqhub.entity.User;
import com.pyqhub.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /** Public: list all questions, paginated */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuestionResponse>>> getAllQuestions(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(questionService.getAllQuestions(pageable)));
    }

    /**
     * Public: global search.
     *
     * Query params (all optional):
     *   keyword, year, courseId, courseName, semesterNumber,
     *   subjectId, subjectName, topicId, topicName, type
     *   page, size, sort
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<QuestionResponse>>> searchQuestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String courseName,
            @RequestParam(required = false) Integer semesterNumber,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) String topicName,
            @RequestParam(required = false) QuestionType type,
            @PageableDefault(size = 20, sort = "year", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<QuestionResponse> result = questionService.searchQuestions(
                keyword, year, courseId, courseName, semesterNumber,
                subjectId, subjectName, topicId, topicName, type, pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /** Public: get single question */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(questionService.getQuestionById(id)));
    }

    /** Authenticated: get my own contributions */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CONTRIBUTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Page<QuestionResponse>>> getMyQuestions(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                questionService.getMyQuestions(currentUser.getCollegeId(), pageable)));
    }

    /** CONTRIBUTOR or ADMIN: add a question */
    @PostMapping
    @PreAuthorize("hasAnyRole('CONTRIBUTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal User currentUser) {
        QuestionResponse response = questionService.createQuestion(
                request, currentUser.getCollegeId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Question added successfully", response));
    }

    /** Author or ADMIN: update a question */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CONTRIBUTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal User currentUser) {
        QuestionResponse response = questionService.updateQuestion(
                id, request, currentUser.getCollegeId());
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", response));
    }

    /** Author or ADMIN: delete a question */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CONTRIBUTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        questionService.deleteQuestion(id, currentUser.getCollegeId());
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
    }
}
