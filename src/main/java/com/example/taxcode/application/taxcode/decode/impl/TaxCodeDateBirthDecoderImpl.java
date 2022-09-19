package com.example.taxcode.application.taxcode.decode.impl;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.taxcode.decode.TaxCodeDateBirthDecoder;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Optional;


@Component
public class TaxCodeDateBirthDecoderImpl implements TaxCodeDateBirthDecoder {
    private static final int FEMALE_FIRST_DAY_MONTH = 41;
    private static final int FEMALE_LAST_DAY_MONTH = 71;
    private static final int MALE_FIRST_DAY_MONTH = 1;
    private static final int MALE_LAST_DAY_MONTH = 31;
    private static HashMap<String, Month> letterMonthValue;

    private TaxCodeDateBirthDecoderImpl() {
        loadMonthCodeValues();
    }

    @Override
    public LocalDate decodeDateOfBirth(DateBirthCode dateBirthCode) {
        birthCodeIsValidOrThrowException(dateBirthCode);
        var year = decodeYearOfBirth(dateBirthCode.getYearOfBirthCode());
        var month = decodeMonthOfBirth(dateBirthCode.getMonthOfBirthCode());
        var day = decodeDayOfBirth(dateBirthCode.getDayOfBirthCode());

        return LocalDate.of(year, month, day);
    }

    @Override
    public Gender decodeGender(DateBirthCode dateBirthCode) {
        birthCodeIsValidOrThrowException(dateBirthCode);

        var dayBirth = Integer.parseInt(dateBirthCode.getDayOfBirthCode());

        if (dayBirth >= MALE_FIRST_DAY_MONTH && dayBirth <= MALE_LAST_DAY_MONTH) {
            return Gender.MAN;
        } else if (dayBirth >= FEMALE_FIRST_DAY_MONTH && dayBirth <= FEMALE_LAST_DAY_MONTH) {
            return Gender.WOMAN;
        }

        throw new IllegalArgumentException("The dateBirthCode provided is not valid. Day of birth: '"
                        + dayBirth + "' not allowed");
    }

    private int decodeYearOfBirth(String yearOfBirthCode) {
        if (!validateYearOfBirthCode(yearOfBirthCode)) {
            throw new DateTimeException("The year of birth provided '" + yearOfBirthCode + "' is not valid");
        }

        var yearOfBirthCodeInteger = Integer.parseInt(yearOfBirthCode);
        var currentYear = LocalDate.now().getYear();
        var lastTwoDigitsCurrentYear = currentYear % 100;
        var currentCentury = currentYear - lastTwoDigitsCurrentYear;
        var yearOfBirth = yearOfBirthCodeInteger > lastTwoDigitsCurrentYear ? (currentCentury - 100) : currentCentury;

        return yearOfBirth + yearOfBirthCodeInteger;
    }

    private boolean validateYearOfBirthCode(String yearOfBirthCode) {
        if (yearOfBirthCode == null || yearOfBirthCode.isBlank()) {
            return false;
        }

        try {
            var yearOfBirth = Integer.parseInt(yearOfBirthCode);
            return yearOfBirth >= 0 && yearOfBirth <= 99;
        } catch (NumberFormatException exception) {
            return false;
        }
    }


    private Month decodeMonthOfBirth(String monthOfBirthCode) throws DateTimeException {
        var month = letterMonthValue.get(monthOfBirthCode.toUpperCase());
        return Optional.ofNullable(month)
                .orElseThrow(() -> new DateTimeException("The month of birth provided is not valid. Month of birth: '"
                        + monthOfBirthCode + "' not allowed"));
    }

    private int decodeDayOfBirth(String dayOfBirthCode) {
        var dayBirthInt = Integer.parseInt(dayOfBirthCode);

        if (dayBirthInt >= MALE_FIRST_DAY_MONTH && dayBirthInt <= MALE_LAST_DAY_MONTH) {
            return dayBirthInt;
        } else if (dayBirthInt >= FEMALE_FIRST_DAY_MONTH && dayBirthInt <= FEMALE_LAST_DAY_MONTH) {
            return dayBirthInt - 40;
        }

        throw new DateTimeException("The day of birth provided is not valid. Day of birth: '"
                + dayOfBirthCode + "' not allowed");
    }

    private void birthCodeIsValidOrThrowException(DateBirthCode dateBirthCode) throws DateTimeException {
        if (!validateBirthCode(dateBirthCode)) {
            throw new DateTimeException("dateBirthCode not valid");
        }
    }

    private boolean validateBirthCode(DateBirthCode dateBirthCode) {
        return dateBirthCode != null && dateBirthCode.getDayOfBirthCode() != null
                && dateBirthCode.getYearOfBirthCode() != null && dateBirthCode.getMonthOfBirthCode() != null;
    }

    private static void loadMonthCodeValues() {
        letterMonthValue = new HashMap<>();
        letterMonthValue.put("A", Month.JANUARY);
        letterMonthValue.put("B", Month.FEBRUARY);
        letterMonthValue.put("C", Month.MARCH);
        letterMonthValue.put("D", Month.APRIL);
        letterMonthValue.put("E", Month.MAY);
        letterMonthValue.put("H", Month.JUNE);
        letterMonthValue.put("L", Month.JULY);
        letterMonthValue.put("M", Month.AUGUST);
        letterMonthValue.put("P", Month.SEPTEMBER);
        letterMonthValue.put("R", Month.OCTOBER);
        letterMonthValue.put("S", Month.NOVEMBER);
        letterMonthValue.put("T", Month.DECEMBER);
    }
}
