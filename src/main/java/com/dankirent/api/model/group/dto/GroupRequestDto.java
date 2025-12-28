package com.dankirent.api.model.group.dto;

import com.dankirent.api.model.permission.dto.PermissionRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record GroupRequestDto(
        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotNull
        @NotEmpty
        Set<PermissionRequestDto> permissions

) {
}
