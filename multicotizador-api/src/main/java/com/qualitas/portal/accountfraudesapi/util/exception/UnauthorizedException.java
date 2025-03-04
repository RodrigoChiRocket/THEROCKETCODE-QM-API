package com.qualitas.portal.accountfraudesapi.util.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}