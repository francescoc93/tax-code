package com.example.taxcode.unit.controller.stream;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.exception.TaxCodeNotValidException;
import org.junit.jupiter.params.provider.Arguments;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class TaxCodeControllerStreamData {

    private static Stream<Arguments> provideInputWithExceptionClass(){
        var person = new Person("", "", Gender.MALE, "Roma",
                LocalDate.of(1980, Month.FEBRUARY, 2));

        var person2 = new Person("Mario", "Rossi", Gender.MALE, "Roma",
                LocalDate.of(1980, Month.FEBRUARY, 2));
        return Stream.of(Arguments.of(person,IllegalArgumentException.class),
                Arguments.of(person2, CityCodeNotFoundException.class));
    }

    private static Stream<Arguments> provideDecodeTaxInputWithExceptionClass(){
        var taxCode = "RSSMRA85B02A562I";

        return Stream.of(Arguments.of(taxCode,IllegalArgumentException.class),
                Arguments.of(taxCode, CityCodeNotFoundException.class),
                Arguments.of(taxCode, TaxCodeNotValidException.class),
                Arguments.of(taxCode,DateTimeException.class));
    }
}
