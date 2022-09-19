package com.example.taxcode.functional.stream;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FunctionalStreamData {
    private static final String NAME = "Mario";
    private static final String SURNAME = "Rossi";
    private static final String PLACE_OF_BIRTH = "Roma";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, Month.JULY, 12);
    private static final Gender GENDER = Gender.MAN;


    private static Stream<People> invalidPeople() {
        return Stream.of(null,
                new People(null, SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, null, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, SURNAME, null, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, SURNAME, GENDER, null, DATE_OF_BIRTH),
                new People(NAME, SURNAME, GENDER, PLACE_OF_BIRTH, null),
                new People(null, null, null, null, null),
                new People(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, " ", GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, SURNAME, GENDER, "", DATE_OF_BIRTH),
                new People(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, " ", GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH),
                new People(NAME, SURNAME, GENDER, "", DATE_OF_BIRTH));
    }

    private static Stream<Arguments> validPeople() {
        var people = new People("Mario","Rossi",Gender.MAN,"Roma",LocalDate.of(1980,Month.JULY,10));
        var taxCode = new TaxCodeResponse("RSSMRA80L10H501Z");
        var people2 = new People("  Giovanna  "," Rossi",Gender.WOMAN,"   Ascoli   PICENO",LocalDate.of(1985,Month.JULY,17));
        var taxCode2 = new TaxCodeResponse("RSSGNN85L57A462R");
        var people3 = new People("Mario Giulio","Rossi Leone",Gender.MAN,"Torino",LocalDate.of(1990,Month.DECEMBER,18));
        var taxCode3 = new TaxCodeResponse("RSSMGL90T18L219N");
        var people4 = new People("Francesca","Li",Gender.WOMAN,"Bologna",LocalDate.of(1990,Month.AUGUST,18));
        var taxCode4 = new TaxCodeResponse("LIXFNC90M58A944F");
        var people5 = new People("Al","Leone",Gender.MAN,"Bologna",LocalDate.of(1990,Month.AUGUST,18));
        var taxCode5 = new TaxCodeResponse("LNELAX90M18A944Y");

        return Stream.of(Arguments.of(people,taxCode),
                Arguments.of(people2,taxCode2),
                Arguments.of(people3,taxCode3),
                Arguments.of(people4,taxCode4),
                Arguments.of(people5,taxCode5));
    }

    private static Stream<String> invalidTaxCode() {
        return Stream.of("  RSSMRA80L10H501Z",
                "RSS MRA 80L10 H501 Z",
                "RSSMRA80L10H501A",
                "RSSMRA80L10H50",
                "RSSMRA80L80H501Z",
                "RSSMRA80710H501Z",
                "RSSMRA80B31H501Z");
    }

    private static Stream<Arguments> validTaxCode() {
        var taxCode = "RSSMRA80L10H501Z";
        var name = new ArrayList<String>();
        name.add("MARIO");
        var surname = new ArrayList<String>();
        surname.add("ROSSI");
        surname.add("ROSSO");
        var taxCodeDecode = TaxCodeDecode.builder()
                .gender(Gender.MAN)
                .placeOfBirth("ROMA")
                .dateOfBirth(LocalDate.of(1980,Month.JULY,10))
                .names(name)
                .surnames(surname)
                .build();

        var taxCode2 = "LNELAX90M18A944Y";
        var taxCodeDecode2 = TaxCodeDecode.builder()
                .gender(Gender.MAN)
                .placeOfBirth("BOLOGNA")
                .dateOfBirth(LocalDate.of(1990,Month.AUGUST,18))
                .names(new ArrayList<>())
                .surnames(new ArrayList<>())
                .build();

        var taxCode3 = "RSSMRA80A41H501Y";
        var name3 = new ArrayList<String>();
        name3.add("MARIA");
        name3.add("MARY");
        var surname3 = new ArrayList<String>();
        surname3.add("ROSSI");
        surname3.add("ROSSO");
        var taxCodeDecode3 = TaxCodeDecode.builder()
                .gender(Gender.WOMAN)
                .placeOfBirth("ROMA")
                .dateOfBirth(LocalDate.of(1980,Month.JANUARY,1))
                .names(name3)
                .surnames(surname3)
                .build();

        return Stream.of(Arguments.of(taxCode,taxCodeDecode),
                Arguments.of(taxCode2,taxCodeDecode2),
                Arguments.of(taxCode3,taxCodeDecode3));
    }
}
