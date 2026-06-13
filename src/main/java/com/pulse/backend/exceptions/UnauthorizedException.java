package com.pulse.backend.exceptions;

public class UnauthorizedException
        extends RuntimeException {

    public UnauthorizedException(
            String message) {

        super(message);
    }
}