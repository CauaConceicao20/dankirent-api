package com.dankirent.api.model.photo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhotoRequestDto(

        @NotBlank
        String fileName,

        @NotBlank
        String contentType,

        @Size(min = 1)
        Long size
) {
}
