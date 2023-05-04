package com.junodx.api.security.exceptions;

public class JWTIDTokenProcessingException extends RuntimeException {
    public JWTIDTokenProcessingException(String s){
        super(s);
    }
}
