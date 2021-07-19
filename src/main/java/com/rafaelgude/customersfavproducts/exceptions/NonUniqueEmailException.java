package com.rafaelgude.customersfavproducts.exceptions;

public class NonUniqueEmailException extends RuntimeException {

    public NonUniqueEmailException(String msg) {
        super(msg);
    }

}
