package com.junodx.api.security.exceptions;

public class InvalidJWTProviderException extends RuntimeException {
    public InvalidJWTProviderException(String s){
        super(s);
    }
}
