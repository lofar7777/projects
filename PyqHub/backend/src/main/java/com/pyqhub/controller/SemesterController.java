package com.pyqhub.controller;

import com.pyqhub.dto.request.SemesterRequest;
import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.entity.Semester;
import com.pyqhub.service.SemesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Semester>>> getSemestersByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                ApiResponse.success(semesterService.getSemestersByCourse(courseId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Semester>> getSemesterById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(semesterService.getSemesterById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Semester>> createSemester(
            @Valid @RequestBody SemesterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Semester created successfully",
                        semesterService.createSemester(request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.ok(ApiResponse.success("Semester deleted successfully", null));
    }
}
