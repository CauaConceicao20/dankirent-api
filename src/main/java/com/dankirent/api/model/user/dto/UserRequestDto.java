package com.dankirent.api.model.user.dto;

import com.dankirent.api.model.user.validation.annotations.Password;
import com.dankirent.api.model.user.validation.annotations.Phone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequestDto(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @CPF
        String cpf,

        @Phone
        String phone,

        @Email
        @NotBlank
        String email,

        @Password
        String password
) {
}
