package com.dankirent.api.model.group.dto;

import com.dankirent.api.model.group.Group;

import java.util.UUID;

public record GroupResponseDto(
        UUID id,
        String name,
        String description
) {
    public GroupResponseDto(Group group) {
        this(
                group.getId(),
                group.getName(),
                group.getDescription()
        );
    }
}
