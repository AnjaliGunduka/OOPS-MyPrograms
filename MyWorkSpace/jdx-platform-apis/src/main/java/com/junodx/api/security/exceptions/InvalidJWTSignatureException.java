package com.junodx.api.security.exceptions;

public class InvalidJWTSignatureException extends RuntimeException {

    public InvalidJWTSignatureException(String s) {
        super(s);
    }
}
