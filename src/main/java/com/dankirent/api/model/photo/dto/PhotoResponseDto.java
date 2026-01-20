package com.dankirent.api.model.photo.dto;


import com.dankirent.api.model.photo.Photo;

import java.time.LocalDateTime;
import java.util.UUID;

public record PhotoResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        LocalDateTime createdAt
) {
    public PhotoResponseDto(Photo photo) {
        this(
                photo.getId(),
                photo.getFileName(),
                photo.getContentType(),
                photo.getSize(),
                photo.getCreatedAt()
        );
    }
}
