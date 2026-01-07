package com.dankirent.api.infrastructure.storage;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FileMetaData {

    private final String fileName;
    private final long size;
    private final String contentType;
    private final LocalDateTime createdAt;

    public FileMetaData(String fileName, long size, String contentType, LocalDateTime createdAt) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.createdAt = createdAt;
    }
}
