package com.assignment.hardship.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private final String errorCode;
    private final String message;
    private final int status; // int
    private final LocalDateTime timestamp; // add timestamp
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> fieldErrors;

    public static ErrorResponse of(ErrorCode errorCode, Map<String, String> fieldErrors) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
