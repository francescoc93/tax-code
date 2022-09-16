package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.taxcode.decode.impl.TaxCodeDateBirthDecoderImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaxCodeDateBirthDecoderTests {
    @InjectMocks
    private TaxCodeDateBirthDecoderImpl taxCodeDateBirthDecoder;

    @DisplayName("decode invalid gender")
    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#birthCodeWithWrongDay")
    void decodeInvalidGender(DateBirthCode dateBirthCode) {
        assertThatThrownBy(() -> {
            taxCodeDateBirthDecoder.decodeGender(dateBirthCode);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("decode invalid month")
    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#birthCodeWithWrongMonth")
    void decodeInvalidMonthBirthDate(DateBirthCode dateBirthCode) {
        assertThatThrownBy(() -> {
            taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode);
        }).isInstanceOf(DateTimeException.class);
    }

    @DisplayName("decode invalid year")
    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#birthCodeWithWrongYear")
    void decodeInvalidYearBirthDate(DateBirthCode dateBirthCode) {
        assertThatThrownBy(() -> {
            taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode);
        }).isInstanceOf(DateTimeException.class);
    }

    @DisplayName("decode invalid day")
    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#birthCodeWithWrongDay")
    void decodeInvalidDayBirthDate(DateBirthCode dateBirthCode) {
        assertThatThrownBy(() -> {
            taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode);
        }).isInstanceOf(DateTimeException.class);
    }

    @DisplayName("decode gender")
    @ParameterizedTest(name = "{index} => dateBirthCode={0}, gender={1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#birthDateStreamData")
    void decodeBirthDate(DateBirthCode dateBirthCode, LocalDate localDate) {
        assertEquals(localDate,taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode));
    }

    @DisplayName("decode birth date")
    @ParameterizedTest(name = "{index} => dateBirthCode={0}, gender={1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthDecoderStreamData#genderStreamData")
    void decodeGender(DateBirthCode dateBirthCode,Gender gender) {
        assertEquals(gender,taxCodeDateBirthDecoder.decodeGender(dateBirthCode));
    }
}
