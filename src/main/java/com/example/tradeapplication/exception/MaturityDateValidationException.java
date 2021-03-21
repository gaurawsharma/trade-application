package com.example.tradeapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class MaturityDateValidationException extends RuntimeException {
    public MaturityDateValidationException(String message) {
        super(message);
    }
}
