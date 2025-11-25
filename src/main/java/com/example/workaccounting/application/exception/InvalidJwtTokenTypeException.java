package com.example.workaccounting.application.exception;

public class InvalidJwtTokenTypeException extends RuntimeException {
    public InvalidJwtTokenTypeException(String message) {
        super(message);
    }
}
