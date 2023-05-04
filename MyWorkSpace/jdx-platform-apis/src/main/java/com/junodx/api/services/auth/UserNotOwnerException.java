package com.junodx.api.services.auth;

public class UserNotOwnerException extends Exception {
    public UserNotOwnerException(String message){
        super(message);
    }
}
