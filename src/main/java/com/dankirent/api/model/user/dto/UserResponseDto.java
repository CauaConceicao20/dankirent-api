package com.dankirent.api.model.user.dto;

import com.dankirent.api.infrastructure.security.UserDetailsImpl;
import com.dankirent.api.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String cpf,
        String phone,
        String email
) {

    public UserResponseDto(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCpf(),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }
}
