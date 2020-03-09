package com.photostudio.exception;

public class GetUserByEmailException extends RuntimeException{
    public GetUserByEmailException(String message) {
        super(message);
    }
}
