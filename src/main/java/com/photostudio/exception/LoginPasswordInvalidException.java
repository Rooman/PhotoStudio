package com.photostudio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPasswordInvalidException extends RuntimeException {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public LoginPasswordInvalidException(String message) {
        super(message);
    }
}
