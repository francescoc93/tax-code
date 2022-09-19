package com.example.taxcode.application.taxcode.generator.impl;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.taxcode.generator.TaxCodeDateBirthGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

@Component
public class TaxCodeDateBirthGeneratorImpl implements TaxCodeDateBirthGenerator {

    private static HashMap<Month, String> letterMonthValue;

    private TaxCodeDateBirthGeneratorImpl() {
        loadMonthCodeValues();
    }

    @Override
    public DateBirthCode calculateDateOfBirthCode(final LocalDate dateOfBirth, Gender gender) {
        if (!validate(dateOfBirth, gender)) {
            throw new IllegalArgumentException("The dateOfBirth or gender are null");
        }

        var yearOfBirth = dateOfBirth.getYear();
        var yearCode = String.valueOf(yearOfBirth).substring(2, 4);
        var monthCode = letterMonthValue.get(dateOfBirth.getMonth());
        var dayCode = calculateDayOfBirthCode(dateOfBirth, gender);

        return new DateBirthCode(yearCode, monthCode, dayCode);
    }

    private boolean validate(LocalDate dateOfBirth, Gender gender) {
        return dateOfBirth != null && gender != null;
    }

    private String calculateDayOfBirthCode(LocalDate dateOfBirth, Gender gender) {
        var dayOfBirth = gender == Gender.MAN ? dateOfBirth.getDayOfMonth() : dateOfBirth.getDayOfMonth() + 40;
        return String.format("%02d", dayOfBirth);
    }

    private static void loadMonthCodeValues() {
        letterMonthValue = new HashMap<>();
        letterMonthValue.put(Month.JANUARY, "A");
        letterMonthValue.put(Month.FEBRUARY, "B");
        letterMonthValue.put(Month.MARCH, "C");
        letterMonthValue.put(Month.APRIL, "D");
        letterMonthValue.put(Month.MAY, "E");
        letterMonthValue.put(Month.JUNE, "H");
        letterMonthValue.put(Month.JULY, "L");
        letterMonthValue.put(Month.AUGUST, "M");
        letterMonthValue.put(Month.SEPTEMBER, "P");
        letterMonthValue.put(Month.OCTOBER, "R");
        letterMonthValue.put(Month.NOVEMBER, "S");
        letterMonthValue.put(Month.DECEMBER, "T");
    }
}
