package com.assignment.hardship.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVICE_ERROR ("INTERNAL_SERVICE_ERROR","An unexpected error occur", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST            ("BAD_REQUEST",           "Invalid Request",           HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
