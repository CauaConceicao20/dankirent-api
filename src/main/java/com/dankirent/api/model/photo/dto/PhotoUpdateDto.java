package com.dankirent.api.model.photo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhotoUpdateDto(
        @NotBlank
        String url,

        @NotBlank
        String description,

        @NotBlank
        String contentType,

        @Size(min = 1)
        Long size
) {
}
