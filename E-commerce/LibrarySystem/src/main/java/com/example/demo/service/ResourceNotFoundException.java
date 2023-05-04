package com.example.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
//    private String resourceName;
//    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(  String fieldValue) {
        super(String.format("%s not found with %s : '%s'", fieldValue));
        
        this.fieldValue = fieldValue;
    }

  

    public Object getFieldValue() {
        return fieldValue;
    }
}
