package com.pyqhub.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction for file storage.
 * Swap the implementation for S3StorageService or GcsStorageService
 * without touching any other class.
 */
public interface FileStorageService {

    /**
     * Store a file and return the public URL or relative path.
     *
     * @param file the multipart file
     * @return URL/path that can be used to retrieve the file
     */
    String store(MultipartFile file);

    /**
     * Delete a stored file.
     *
     * @param fileUrl the URL/path returned by {@link #store}
     */
    void delete(String fileUrl);
}
