package com.dankirent.api.model.auth;

import com.dankirent.api.infrastructure.security.UserDetailsImpl;

import java.util.Collection;
import java.util.UUID;

public record UserLoggedResponseDto(
        UUID id,
        String email,
        Collection<?> roles
) {

    public UserLoggedResponseDto(UserDetailsImpl userDetails) {
        this(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );
    }
}
