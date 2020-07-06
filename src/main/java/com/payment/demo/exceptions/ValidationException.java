package com.payment.demo.exceptions;


/**
 * Custom Validation Exception to be thrown when invalid parameters provided.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
