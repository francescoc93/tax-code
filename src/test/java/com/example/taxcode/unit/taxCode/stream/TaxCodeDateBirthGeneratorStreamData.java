package com.example.taxcode.unit.taxCode.stream;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class TaxCodeDateBirthGeneratorStreamData {
    private static Stream<Arguments> calculateBirthCodeThrowException() {
        return Stream.of(Arguments.of(null, null),
                Arguments.of(null, Gender.MAN),
                Arguments.of(LocalDate.of(1990, Month.JULY, 30), null));
    }

    private static Stream<Arguments> calculateBirthCode() {
        var argumentsArray = new Arguments[74];
        int femaleDayOffset = 40;
        var yearCode = "90";
        var monthCode = "A";
        var dayCode = "12";

        for (int day = 1; day <= 31; day++) {
            var birthDate = LocalDate.of(1990, Month.JANUARY, day);
            var maleDayCode = day < 10 ? ("0" + day) : String.valueOf(day);

            argumentsArray[day - 1] = Arguments.of(birthDate, Gender.MAN, new DateBirthCode(yearCode, monthCode, maleDayCode));
        }

        for (int day = 1; day <= 31; day++) {
            var birthDate = LocalDate.of(1990, Month.JANUARY, day);
            var femaleDayCode = String.valueOf(day + femaleDayOffset);

            argumentsArray[day+30] = Arguments.of(birthDate, Gender.WOMAN, new DateBirthCode(yearCode, monthCode, femaleDayCode));
        }

        argumentsArray[62] = Arguments.of(LocalDate.of(1990, Month.JANUARY, 12), Gender.MAN, new DateBirthCode(yearCode, "A", dayCode));
        argumentsArray[63] = Arguments.of(LocalDate.of(1990, Month.FEBRUARY, 12), Gender.MAN, new DateBirthCode(yearCode, "B", dayCode));
        argumentsArray[64] = Arguments.of(LocalDate.of(1990, Month.MARCH, 12), Gender.MAN, new DateBirthCode(yearCode, "C", dayCode));
        argumentsArray[65] = Arguments.of(LocalDate.of(1990, Month.APRIL, 12), Gender.MAN, new DateBirthCode(yearCode, "D", dayCode));
        argumentsArray[66] = Arguments.of(LocalDate.of(1990, Month.MAY, 12), Gender.MAN, new DateBirthCode(yearCode, "E", dayCode));
        argumentsArray[67] = Arguments.of(LocalDate.of(1990, Month.JUNE, 12), Gender.MAN, new DateBirthCode(yearCode, "H", dayCode));
        argumentsArray[68] = Arguments.of(LocalDate.of(1990, Month.JULY, 12), Gender.MAN, new DateBirthCode(yearCode, "L", dayCode));
        argumentsArray[69] = Arguments.of(LocalDate.of(1990, Month.AUGUST, 12), Gender.MAN, new DateBirthCode(yearCode, "M", dayCode));
        argumentsArray[70] = Arguments.of(LocalDate.of(1990, Month.SEPTEMBER, 12), Gender.MAN, new DateBirthCode(yearCode, "P", dayCode));
        argumentsArray[71] = Arguments.of(LocalDate.of(1990, Month.OCTOBER, 12), Gender.MAN, new DateBirthCode(yearCode, "R", dayCode));
        argumentsArray[72] = Arguments.of(LocalDate.of(1990, Month.NOVEMBER, 12), Gender.MAN, new DateBirthCode(yearCode, "S", dayCode));
        argumentsArray[73] = Arguments.of(LocalDate.of(1990, Month.DECEMBER, 12), Gender.MAN, new DateBirthCode(yearCode, "T", dayCode));

        return Stream.of(argumentsArray);
    }
}
