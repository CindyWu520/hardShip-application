package com.assignment.hardship.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        Map<String, String> validationErrors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, duplicate) -> existing
                ));
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, validationErrors));
    }

    @ExceptionHandler(HardshipException.class)
    public ResponseEntity<ErrorResponse> handleHardshipException(
            HardshipException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("Hardship exception: code={}, message={}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVICE_ERROR.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVICE_ERROR));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNoStaticResource() {
        // intentionally empty - ignore missing static resource like favicon.ico
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Bad Credentials Exception: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.BAD_CREDENTIAL.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.BAD_CREDENTIAL));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.ACCESS_DENIED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.error("Illegal argument exception: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST));
    }
}
