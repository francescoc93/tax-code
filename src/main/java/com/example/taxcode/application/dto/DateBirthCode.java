package com.example.taxcode.application.dto;

import lombok.Data;

@Data
public class DateBirthCode {
    private final String yearOfBirthCode;
    private final String monthOfBirthCode;
    private final String dayOfBirthCode;

    @Override
    public String toString() {
        return yearOfBirthCode + monthOfBirthCode + dayOfBirthCode;
    }
}
