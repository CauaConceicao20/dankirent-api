package com.dankirent.api.model.user.dto;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String phone,
        String email
) {
}
