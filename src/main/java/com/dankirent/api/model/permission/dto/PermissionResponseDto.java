package com.dankirent.api.model.permission.dto;

public record PermissionResponseDto(
        Long id,
        String name,
        String description
) {
}
