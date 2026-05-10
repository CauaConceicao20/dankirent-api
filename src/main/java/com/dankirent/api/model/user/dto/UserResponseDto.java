package com.dankirent.api.model.user.dto;

import com.dankirent.api.model.user.User;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String cpf,
        LocalDate birthday,
        String phone,
        String email,
        String photo_name
) {

    public UserResponseDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCpf(),
                user.getBirthday(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPhoto().getFileName()
        );
    }
}
