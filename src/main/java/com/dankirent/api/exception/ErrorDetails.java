package com.dankirent.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDetails {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime time;

    public ErrorDetails(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.time = LocalDateTime.now();
    }
}