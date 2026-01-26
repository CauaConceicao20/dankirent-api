package com.dankirent.api.model.auth;

import com.dankirent.api.model.user.validation.annotations.Password;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank
        String email,

        @Password
        String password
) {
}
