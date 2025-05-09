package com.hospital.exception;

public class HospitalException extends RuntimeException {
    private final ErrorCode errorCode;

    public HospitalException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HospitalException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        DATABASE_ERROR,
        VALIDATION_ERROR,
        NOT_FOUND,
        DUPLICATE_ENTRY,
        UNAUTHORIZED,
        INVALID_OPERATION,
        SYSTEM_ERROR
    }
} 