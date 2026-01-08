package com.dankirent.api.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldErrorResponse {
    private String field;
    private String message;

    public FieldErrorResponse(String field, String message) {
        this.field = field;
        this.message = message;
    }
}