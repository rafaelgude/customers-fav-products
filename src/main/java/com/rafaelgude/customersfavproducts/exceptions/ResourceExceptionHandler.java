package com.rafaelgude.customersfavproducts.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Errors> handleEntityNotFound(EntityNotFoundException ex) {
        var error = new Error(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(new Errors(List.of(error)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonUniqueEmailException.class)
    public ResponseEntity<Errors> handleNonUniqueEmail(NonUniqueEmailException ex) {
        var error = new Error(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(new Errors(List.of(error)), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errorList = ex.getBindingResult().getFieldErrors()
                .stream().map(x -> new Error(HttpStatus.BAD_REQUEST, x.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new Errors(errorList), HttpStatus.BAD_REQUEST);
    }

}
