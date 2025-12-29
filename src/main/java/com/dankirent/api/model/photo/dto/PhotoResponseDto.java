package com.dankirent.api.model.photo.dto;


import java.time.LocalDateTime;

public record PhotoResponseDto(
        Long id,
        String fileName,
        String contentType,
        Long size,
        LocalDateTime createdAt
) {
}
