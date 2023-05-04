package com.junodx.api.services.exceptions;

import org.springframework.http.HttpStatus;

public class ShippingAddressNotFoundException extends JdxServiceException {
    public static final int CODE = 4000;
    public static final HttpStatus HTTP_STATUS_CODE = HttpStatus.BAD_REQUEST;
    public static final String message = "Cannot find shipping information required for this request.";

    public ShippingAddressNotFoundException(String msg) {
        super(msg);
    }
}
