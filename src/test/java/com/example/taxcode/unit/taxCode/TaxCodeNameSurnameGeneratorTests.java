package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.taxcode.generator.impl.TaxCodeNameSurnameGeneratorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaxCodeNameSurnameGeneratorTests {

    @InjectMocks
    private TaxCodeNameSurnameGeneratorImpl taxCodeNameSurnameGenerator;


    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeNameSurnameGeneratorStreamData#invalidInputThrowException")
    @DisplayName("Calculate name code throw exception due to invalid input")
    void calculateNameCodeThrowException(String name) {
        assertThatThrownBy(() -> {
            taxCodeNameSurnameGenerator.calculateNameCode(name);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeNameSurnameGeneratorStreamData#invalidInputThrowException")
    @DisplayName("Calculate surname code throw exception due to invalid input")
    void calculateSurnameCodeThrowException(String surname) {
        assertThatThrownBy(() -> {
            taxCodeNameSurnameGenerator.calculateSurnameCode(surname);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeNameSurnameGeneratorStreamData#calculateNameCode")
    @DisplayName("Calculate name code")
    void calculateNameCode(String name, String expectedNameCode) {
        var nameCode = taxCodeNameSurnameGenerator.calculateNameCode(name);
        assertEquals(expectedNameCode,nameCode);
    }

    @ParameterizedTest(name = "{index} => dateOfBirth={0}, gender = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeNameSurnameGeneratorStreamData#calculateSurnameCode")
    @DisplayName("Calculate surname code")
    void calculateSurnameCode(String surname, String expectedSurnameCode) {
        var surnameCode = taxCodeNameSurnameGenerator.calculateSurnameCode(surname);
        assertEquals(expectedSurnameCode,surnameCode);
    }
}
