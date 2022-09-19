package com.example.taxcode.unit.taxCode.stream;


import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.People;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class TaxCodeGeneratorStreamData {


    private static Stream<People> invalidPeople() {
        var name = "Mario";
        var surname = "Rossi";
        var placeOfBirth = "Roma";
        var dateOfBirth = LocalDate.of(1990, Month.JULY, 12);
        var gender = Gender.MAN;

        return Stream.of(null,
                new People(null, surname, gender, placeOfBirth, dateOfBirth),
                new People(name, null, gender, placeOfBirth, dateOfBirth),
                new People(name, surname, null, placeOfBirth, dateOfBirth),
                new People(name, surname, gender, null, dateOfBirth),
                new People(name, surname, gender, placeOfBirth, null),
                new People(null, null, null, null, null));
    }

}
