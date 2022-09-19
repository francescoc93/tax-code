package com.example.taxcode.unit.taxCode.stream;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

public class TaxCodeDateBirthDecoderStreamData {
    private static Stream<DateBirthCode> birthCodeWithWrongDay() {
        return Stream.of(new DateBirthCode("93", "B", "999"),
                new DateBirthCode("93", "B", "80"),
                new DateBirthCode("93", "B", "36"));

    }

    private static Stream<DateBirthCode> birthCodeWithWrongMonth() {
        return Stream.of(new DateBirthCode("93", "Z", "30"),
                new DateBirthCode("93", "J", "71"),
                new DateBirthCode("93", null, "71"),
                new DateBirthCode("93", "UT", "25"));

    }


    private static Stream<DateBirthCode> birthCodeWithWrongYear() {
        return Stream.of(new DateBirthCode("-1", "B", "30"),
                new DateBirthCode("101", "B", "71"),
                new DateBirthCode(null, "B", "71"),
                new DateBirthCode("-20", "B", "25"));

    }

    private static Stream<Arguments> genderStreamData() {
        return Stream.of(Arguments.of(new DateBirthCode("93", "C", "02"), Gender.MAN),
                Arguments.of(new DateBirthCode("93", "C", "41"),Gender.WOMAN),
                Arguments.of(new DateBirthCode("93", "C", "31"),Gender.MAN),
                Arguments.of(new DateBirthCode("93", "C", "71"),Gender.WOMAN));

    }

    private static Stream<Arguments> birthDateStreamData() {
        var dateBirthCode1 = new DateBirthCode("05", "B", "02");
        var dateBirthCode2 = new DateBirthCode("93", "A", "71");
        var dateBirthCode3 = new DateBirthCode("07","t","31");
        var localDate1 = LocalDate.of(2005, Month.FEBRUARY,2);
        var localDate2 = LocalDate.of(1993, Month.JANUARY,31);
        var localDate3 = LocalDate.of(2007, Month.DECEMBER,31);

        return Stream.of(Arguments.of(dateBirthCode1,localDate1 ),
                Arguments.of(dateBirthCode2,localDate2),
                Arguments.of(dateBirthCode3,localDate3));

    }
}
