package com.junodx.api.services.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCartItemsException extends JdxServiceException {
    public static final int CODE = 4000;
    public static final HttpStatus HTTP_STATUS_CODE = HttpStatus.NOT_FOUND;
    public static final String message = "Cannot find requested user to a required user context was not provided.";

    public InvalidCartItemsException(String msg) {
        super(msg);
    }
}
