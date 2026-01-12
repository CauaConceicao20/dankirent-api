package com.dankirent.api.model.group.dto;

public record GroupUpdateDto(
        String name,
        String description
) {

    public GroupUpdateDto {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException("O nome do grupo não pode ser nulo nem ter menos de 3 caracteres.");
        }
        if(description == null || name.length() < 10) {
            throw new IllegalArgumentException("A descrição do grupo não pode ser nula ou ter menos de 10 caracteres.");
        }
    }
}
