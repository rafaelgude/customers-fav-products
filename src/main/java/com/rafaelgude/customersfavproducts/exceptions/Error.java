package com.rafaelgude.customersfavproducts.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Error {
    private int status;
    private String message;

    public Error(HttpStatus httpStatus, String message) {
        super();
        this.status = httpStatus.value();
        this.message = message;
    }
}
