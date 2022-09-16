package com.example.taxcode.unit.taxCode.stream;


import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.Person;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class TaxCodeGeneratorStreamData {


    private static Stream<Person> invalidPerson() {
        var name = "Mario";
        var surname = "Rossi";
        var placeOfBirth = "Roma";
        var dateOfBirth = LocalDate.of(1990, Month.JULY, 12);
        var gender = Gender.MALE;

        return Stream.of(null,
                new Person(null, surname, gender, placeOfBirth, dateOfBirth),
                new Person(name, null, gender, placeOfBirth, dateOfBirth),
                new Person(name, surname, null, placeOfBirth, dateOfBirth),
                new Person(name, surname, gender, null, dateOfBirth),
                new Person(name, surname, gender, placeOfBirth, null),
                new Person(null, null, null, null, null));
    }

}
