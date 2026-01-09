package com.dankirent.api.exception;

import com.dankirent.api.exception.personalized.FieldValidationException;
import com.dankirent.api.exception.personalized.StorageException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<String, FieldErrorResponse> CONSTRAINT_ERRORS = Map.of(
            "users_cpf_key", new FieldErrorResponse("cpf", "CPF já cadastrado"),
            "users_email_key", new FieldErrorResponse("email", "E-mail já cadastrado")
    );

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDetails> handleStorageException(
            StorageException ex,
            HttpServletRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Storage Error",
                List.of(new FieldErrorResponse(
                        "_global",
                        ex.getMessage()
                )),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorDetails> handleFieldValidationException(
            FieldValidationException ex,
            HttpServletRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.BAD_REQUEST.value(),
                "Campo inválido",
                List.of(new FieldErrorResponse(
                        ex.getField(),
                        ex.getMessage()
                )),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                List.of(new FieldErrorResponse(
                        "_global",
                        ex.getMessage()
                )),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String title = "Conflito de dados";
        List<FieldErrorResponse> errors =
                List.of(new FieldErrorResponse("unknown", "Violação de integridade de dados"));

        Throwable cause = ex.getCause();
        if (cause instanceof ConstraintViolationException cve) {
            FieldErrorResponse fieldError =
                    CONSTRAINT_ERRORS.getOrDefault(
                            cve.getConstraintName(),
                            new FieldErrorResponse("unknown", "Violação de integridade de dados")
                    );

            errors = List.of(fieldError);
        }

        ErrorDetails errorDetails = new ErrorDetails(
                HttpStatus.CONFLICT.value(),
                title,
                errors,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }
}
