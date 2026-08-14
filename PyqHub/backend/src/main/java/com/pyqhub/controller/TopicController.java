package com.pyqhub.controller;

import com.pyqhub.dto.request.TopicRequest;
import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.entity.Topic;
import com.pyqhub.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<Topic>>> getTopicsBySubject(
            @PathVariable Long subjectId) {
        return ResponseEntity.ok(
                ApiResponse.success(topicService.getTopicsBySubject(subjectId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Topic>> getTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(topicService.getTopicById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Topic>> createTopic(
            @Valid @RequestBody TopicRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Topic created successfully",
                        topicService.createTopic(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Topic>> updateTopic(
            @PathVariable Long id,
            @Valid @RequestBody TopicRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Topic updated successfully",
                        topicService.updateTopic(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok(ApiResponse.success("Topic deleted successfully", null));
    }
}
