package com.dankirent.api.model.user.dto;

import com.dankirent.api.model.photo.dto.PhotoResponseDto;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String cpf,
        String phone,
        String email,
        PhotoResponseDto photo
) {
}
