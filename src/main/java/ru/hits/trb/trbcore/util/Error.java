package ru.hits.trb.trbcore.util;

import org.springframework.http.HttpStatus;

public enum Error {
    INTERNAL_ERROR(0, HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(1, HttpStatus.BAD_REQUEST),
    INCORRECT_ACCOUNT_TYPE_WHEN_CREATING(2, HttpStatus.BAD_REQUEST);

    public final int code;
    public final HttpStatus httpStatus;

    Error(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
