package com.dankirent.api.model.photo.dto;


public record PhotoResponseDto(
        Long id,
        String url,
        String description,
        String contentType,
        Long size
) {
}
