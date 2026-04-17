package com.assignment.hardship.exception;

import lombok.Getter;

@Getter
public class HardShipException extends RuntimeException {
    private final ErrorCode errorCode;

    public HardShipException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
