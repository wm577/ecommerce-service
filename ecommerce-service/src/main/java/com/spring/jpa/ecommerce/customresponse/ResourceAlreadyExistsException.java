package com.spring.jpa.ecommerce.customresponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT) // This maps the exception to 409 Conflict
public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}