package com.dankirent.api.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ErrorDetails {
    private int status;
    private String error;
    private List<FieldErrorResponse> message;
    private String path;
    private LocalDateTime time;

    public ErrorDetails(int status, String error, List<FieldErrorResponse> message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.time = LocalDateTime.now();
    }
}