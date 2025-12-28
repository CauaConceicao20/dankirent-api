package com.dankirent.api.model.permission.dto;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequestDto(
        @NotBlank
        String name,

        @NotBlank
        String description
) {
}
