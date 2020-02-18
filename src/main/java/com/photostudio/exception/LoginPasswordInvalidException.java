package com.photostudio.exception;

public class LoginPasswordInvalidException extends RuntimeException {
    public LoginPasswordInvalidException(String message) {
        super(message);
    }
}
