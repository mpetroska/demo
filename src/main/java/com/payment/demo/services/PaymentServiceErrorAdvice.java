package com.payment.demo.services;

import com.payment.demo.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handles exceptions mapping to REST response.
 */
@Slf4j
@ControllerAdvice
public class PaymentServiceErrorAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException (ValidationException e) {
        return error(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return error(HttpStatus.NOT_FOUND, e);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
