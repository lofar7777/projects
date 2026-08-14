package com.pyqhub.service;

import com.pyqhub.exception.BadRequestException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Local filesystem implementation of FileStorageService.
 * To switch to S3, implement FileStorageService with an S3StorageService
 * class and annotate it with @Primary (or remove this @Service annotation).
 */
@Service
@Slf4j
public class LocalFileStorageService implements FileStorageService {

    private static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "application/pdf"
    );

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
            log.info("File storage directory initialized at: {}", rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create file upload directory: " + uploadDir, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Cannot upload an empty file");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException(
                    "File type not allowed. Allowed types: JPEG, PNG, GIF, PDF");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String storedFilename = UUID.randomUUID() + extension;
        Path targetPath = rootLocation.resolve(storedFilename);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Stored file: {}", storedFilename);
            // Return a relative URL; the FileController will serve these files
            return "/api/files/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + originalFilename, e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/api/files/")) return;
        String filename = fileUrl.replace("/api/files/", "");
        Path filePath = rootLocation.resolve(filename).normalize();
        try {
            Files.deleteIfExists(filePath);
            log.debug("Deleted file: {}", filename);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", filename, e);
        }
    }

    /** Used by FileController to load the raw file bytes for download. */
    public Path load(String filename) {
        return rootLocation.resolve(filename).normalize();
    }
}
