package com.dankirent.api.model.group.dto;

import com.dankirent.api.model.permission.dto.PermissionRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record GroupRequestDto(
        @Size(min = 3)
        String name,

        @Size(min = 10)
        String description
) {
}
