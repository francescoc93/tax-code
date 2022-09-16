package com.example.taxcode.application.exception;

public class CityCodeNotFoundException extends RuntimeException{
    public CityCodeNotFoundException(String msg){
        super(msg);
    }
}
