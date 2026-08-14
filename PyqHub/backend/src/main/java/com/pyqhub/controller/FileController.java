package com.pyqhub.controller;

import com.pyqhub.dto.response.ApiResponse;
import com.pyqhub.service.FileStorageService;
import com.pyqhub.service.LocalFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;
    private final LocalFileStorageService localFileStorageService;

    /**
     * Upload a file (JPEG, PNG, GIF, or PDF).
     * Returns the URL to include in a QuestionRequest.fileUrl.
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('CONTRIBUTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.store(file);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully",
                Map.of("fileUrl", fileUrl)));
    }

    /**
     * Serve / download a stored file.
     * This endpoint is public so unauthenticated users can view question attachments.
     */
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = localFileStorageService.load(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = determineContentType(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("File download failed for: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".gif")) return "image/gif";
        return "image/jpeg"; // default fallback
    }
}
