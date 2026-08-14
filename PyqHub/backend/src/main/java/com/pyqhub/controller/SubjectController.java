package com.pyqhub.controller;

import com.pyqhub.dto.request.SubjectRequest;
import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.entity.Subject;
import com.pyqhub.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<Subject>>> getSubjectsBySemester(
            @PathVariable Long semesterId) {
        return ResponseEntity.ok(
                ApiResponse.success(subjectService.getSubjectsBySemester(semesterId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Subject>> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getSubjectById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Subject>> createSubject(
            @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject created successfully",
                        subjectService.createSubject(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Subject>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Subject updated successfully",
                        subjectService.updateSubject(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success("Subject deleted successfully", null));
    }
}
