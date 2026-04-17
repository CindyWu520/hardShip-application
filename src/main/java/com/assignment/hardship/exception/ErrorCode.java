package com.assignment.hardship.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVICE_ERROR("INTERNAL_SERVICE_ERROR", "An unexpected error occur", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("BAD_REQUEST", "Invalid Request", HttpStatus.BAD_REQUEST),
    HARDSHIP_ALREADY_EXISTS("HARDSHIP_ALREADY_EXISTS", "Hardship already exists", HttpStatus.CONFLICT),
    HARDSHIP_NOT_FOUND("HARDSHIP_NOT_FOUND", "Can't find target hardship record", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
