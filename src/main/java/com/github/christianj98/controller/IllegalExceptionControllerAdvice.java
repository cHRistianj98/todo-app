package com.github.christianj98.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// This controller takes into account only these classes which are annotated by IllegalExceptionProcessing.class
@RestControllerAdvice(annotations = IllegalExceptionProcessing.class)
public class IllegalExceptionControllerAdvice {

    /*
    These exception handlers are invoked if exceptions will be thrown
 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
