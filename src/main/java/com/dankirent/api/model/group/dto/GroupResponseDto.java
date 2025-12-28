package com.dankirent.api.model.group.dto;

public record GroupResponseDto(
        Long id,
        String name,
        String description,
        Integer quantity
) {
}
