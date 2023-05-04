package com.junodx.api.services.auth;

public class UserNotInClientException extends Exception {
    public UserNotInClientException(String message){
        super(message);
    }
}
