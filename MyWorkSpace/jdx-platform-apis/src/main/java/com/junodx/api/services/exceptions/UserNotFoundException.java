package com.junodx.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends JdxServiceException {
    public static final int CODE = 4004;
    public static final HttpStatus HTTP_STATUS_CODE = HttpStatus.NOT_FOUND;
    public static final String message = "Cannot find requested user to a required user context was not provided.";

    public UserNotFoundException(String msg) {
        super(msg);
        this.code = CODE;
        this.statusCode = HTTP_STATUS_CODE;
        this.label = message;
        this.details = msg;
    }
}
