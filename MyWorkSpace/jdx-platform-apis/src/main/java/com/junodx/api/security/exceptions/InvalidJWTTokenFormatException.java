package com.junodx.api.security.exceptions;

public class InvalidJWTTokenFormatException extends RuntimeException {
    public InvalidJWTTokenFormatException(String s){
        super(s);
    }
}
