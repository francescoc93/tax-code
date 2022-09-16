package com.example.taxcode.application.exception;

public class TaxCodeNotValidException extends RuntimeException {
    public TaxCodeNotValidException(String msg) {
        super(msg);
    }
}
