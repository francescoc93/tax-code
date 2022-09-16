package com.example.taxcode.unit.taxCode;


import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.taxcode.generator.impl.TaxCodeDateBirthGeneratorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaxCodeDateBirthGeneratorTests {
    @InjectMocks
    private TaxCodeDateBirthGeneratorImpl taxCodeDateBirthGenerator;

    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthGeneratorStreamData#calculateBirthCodeThrowException")
    @DisplayName("Calculate birth code throw exception due to invalid input")
    void calculateBirthCodeThrowException(LocalDate dateOfBirth, Gender gender) {
        assertThatThrownBy(() -> {
            taxCodeDateBirthGenerator.calculateDateOfBirthCode(dateOfBirth, gender);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}, expectedBirthCode = {2}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeDateBirthGeneratorStreamData#calculateBirthCode")
    @DisplayName("Calculate birth code")
    void calculateBirthCode(LocalDate dateOfBirth, Gender gender, DateBirthCode expectedBirthCode) {
        var birthCode = taxCodeDateBirthGenerator.calculateDateOfBirthCode(dateOfBirth, gender);

        assertEquals(expectedBirthCode.getYearOfBirthCode(), birthCode.getYearOfBirthCode());
        assertEquals(expectedBirthCode.getMonthOfBirthCode(), birthCode.getMonthOfBirthCode());
        assertEquals(expectedBirthCode.getDayOfBirthCode(), birthCode.getDayOfBirthCode());
    }
}
