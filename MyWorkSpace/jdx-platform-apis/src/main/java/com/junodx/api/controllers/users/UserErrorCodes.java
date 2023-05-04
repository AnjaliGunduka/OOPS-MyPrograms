package com.junodx.api.controllers.users;

import com.junodx.api.controllers.commerce.CheckoutErrorCodes;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum UserErrorCodes {
    //Account
    CUSTOMER_NOT_PROVIDED(HttpStatus.BAD_REQUEST, 4101, "Cannot find customer to associate with checkout request."),

    //Password
    PASSWORD_DOES_NOT_ABIDE_RULES(HttpStatus.BAD_REQUEST, 1101, "There was an issue with the user's password."),
    PASSWORDS_DO_NOT_MATCH(HttpStatus.BAD_REQUEST, 1102, "There was an issue with the user's password.");


    private static final Map<HttpStatus, UserErrorCodes> BY_STATUS_CODE = new HashMap<>();
    private static final Map<Integer, UserErrorCodes> BY_CODE = new HashMap<>();
    private static final Map<String, UserErrorCodes> BY_MESSAGE = new HashMap<>();

    static {
        for (UserErrorCodes e : values()) {
            BY_STATUS_CODE.put(e.statusCode, e);
            BY_CODE.put(e.code, e);
            BY_MESSAGE.put(e.message, e);
        }
    }

    public final HttpStatus statusCode;
    public final int code;
    public final String message;

    private UserErrorCodes(org.springframework.http.HttpStatus status, int code, String msg) {
        this.statusCode = status;
        this.code = code;
        this.message = msg;
    }

    public static UserErrorCodes valueOfStatusCode(HttpStatus statusCode) {
        return BY_STATUS_CODE.get(statusCode);
    }

    public static UserErrorCodes valueOfCode(int code) {
        return BY_CODE.get(code);
    }

    public static UserErrorCodes valueOfMessage(String message) {
        return BY_MESSAGE.get(message);
    }
}
