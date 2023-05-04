package com.junodx.api.services.exceptions;

import com.junodx.api.logging.LogCode;
import org.springframework.http.HttpStatus;

public class JdxServiceException extends RuntimeException {
    public static final int GENERAL_ERROR_CODE = 4000;
    public static final HttpStatus HTTP_STATUS_CODE = HttpStatus.BAD_REQUEST;
    public static final String errorMessage = "The system experienced an error processing the client's request.";

    private String message;
    protected String label;
    protected int code;
    protected HttpStatus statusCode;
    protected String details;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        this.message = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public JdxServiceException(String msg){
        super(msg);
        this.message = errorMessage;
        this.label = errorMessage;
        this.details = msg;
        this.code = GENERAL_ERROR_CODE;
        this.statusCode = HTTP_STATUS_CODE;
    }

    public JdxServiceException(int code, HttpStatus statusCode, String details){
        super(errorMessage);
        this.message = errorMessage;
        this.label = errorMessage;
        this.details = details;
        this.code = GENERAL_ERROR_CODE;
        this.statusCode = HTTP_STATUS_CODE;
    }

    public JdxServiceException(int code, HttpStatus statusCode, String msg, String details){
        super(msg);
        this.message = msg;
        this.label = msg;
        this.details = details;
        this.code = code;
        this.statusCode = statusCode;
    }

}
