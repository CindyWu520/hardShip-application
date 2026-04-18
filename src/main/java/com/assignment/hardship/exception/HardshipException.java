package com.assignment.hardship.exception;

import lombok.Getter;

@Getter
public class HardshipException extends RuntimeException {
    private final ErrorCode errorCode;

    public HardshipException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
