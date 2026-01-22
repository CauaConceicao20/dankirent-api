package com.dankirent.api.model.photo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PhotoUpdateDto(
        @NotBlank
        String fileName,

        @NotBlank
        String contentType,

        @Size(min = 1)
        Long size
) {
    public PhotoUpdateDto(MultipartFile file) {
        this(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );
    }
}
