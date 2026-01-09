package com.dankirent.api.model.user.dto;

import com.dankirent.api.exception.personalized.FieldValidationException;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String phone
) {
    public UserUpdateDto {

        if (firstName != null && firstName.isBlank() || firstName != null && firstName.length() < 3)
            throw new FieldValidationException("firstName","Primeiro nome não pode ser vazio");

        if (lastName != null && lastName.isBlank() || lastName != null && lastName.length() < 3)
            throw new FieldValidationException("lastName", "Sobrenome não pode ser vazio");

        if (phone != null) {
            if (phone.isBlank())
                throw new FieldValidationException("phone", "Telefone não pode ser vazio");

            String normalized = phone.replaceAll("\\D", "");

            if (!normalized.matches("^\\d{11}$"))
                throw new FieldValidationException("phone", "Telefone inválido");

            phone = normalized;
        }
    }
}
