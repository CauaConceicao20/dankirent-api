package com.dankirent.api.model.user.dto;

import com.dankirent.api.exception.personalized.FieldValidationException;
import com.dankirent.api.model.user.validation.annotations.Password;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequestDto(

        @NotBlank
        @Size(min = 3)
        String firstName,

        @NotBlank
        @Size(min = 3)
        String lastName,

        @CPF(message = "CPF inválido")
        String cpf,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @Past(message = "Data de nascimento deve ser no passado")
        @NotBlank
        LocalDate birthday,

        String phone,

        @Email(message = "E-mail inválido")
        @NotBlank
        String email,

        @Password
        String password
) {
    public UserRequestDto {

        if (phone != null && !phone.isBlank()) {

            String normalized = phone.replaceAll("\\D", "");

            if (!normalized.matches("^\\d{11}$")) throw new FieldValidationException("phone", "Telefone inválido");

            phone = normalized;

        } else {
            throw new FieldValidationException("phone", "Telefone não pode ser vazio");
        }
    }
    }
