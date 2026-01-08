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

        @CPF(message = "CPF inválido")
        String cpf,

        @Phone
        String phone,

        @Email(message = "E-mail inválido")
        @NotBlank
        String email,

        @Password
        String password
) {
}
