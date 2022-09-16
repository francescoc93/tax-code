package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.taxcode.utils.StringConverter;
import com.example.taxcode.application.taxcode.utils.TaxCodeValidationImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCodeValidationTests {
    @InjectMocks
    private TaxCodeValidationImpl taxCodeValidation;

    @Mock
    private StringConverter stringConverter;

    @ParameterizedTest
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeValidationStreamData#taxCodeNotMatchRegex")
    @DisplayName("Parsing failed into the TaxCode object due to invalid string")
    void parseTaxCodeFail(String taxCode) {
        assertThatThrownBy(() -> {
            taxCodeValidation.parseTaxCode(taxCode);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Parsing from string to TaxCode")
    void parseTaxCode() {
        var taxCodeString = "RssMrA85t30C562i";
        var taxCode = TaxCode.builder()
                .nameCode("MRA")
                .surnameCode("RSS")
                .dateOfBirthCode(new DateBirthCode("85", "T", "30"))
                .cityCode("C562")
                .validationCode('I')
                .build();

        var taxCodeReturned = taxCodeValidation.parseTaxCode(taxCodeString);

        assertThat(taxCode).usingRecursiveComparison().isEqualTo(taxCodeReturned);
    }

    @ParameterizedTest
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeValidationStreamData#taxCodeNotMatchRegex")
    @DisplayName("Tax code is not valid due to no match with the regex")
    void taxCodeIsValidThrowException(String taxCode) {
        assertThatThrownBy(() -> {
            taxCodeValidation.taxCodeIsValid(taxCode);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Tax code is not valid because the calculated validation character it's not equal")
    void taxCodeIsNotValid() {
        var taxCode = "RSSMRA85T10A562A";
        var calculatedValues = new int[]{8, 18, 12, 12, 8, 0, 19, 5, 14, 1, 1, 0, 13, 6, 5};
        when(stringConverter.convertStringInTaxCodeIntegerValues(taxCode.substring(0, 15))).thenReturn(calculatedValues);
        assertFalse(taxCodeValidation.taxCodeIsValid(taxCode));
    }


    @Test
    @DisplayName("The calculated validation character it's equal with the tax code provided")
    void taxCodeIsValid() {
        var taxCode = "RSSMRA85T10A562S";
        int[] calculatedValues = new int[]{8, 18, 12, 12, 8, 0, 19, 5, 14, 1, 1, 0, 13, 6, 5};
        when(stringConverter.convertStringInTaxCodeIntegerValues(taxCode.substring(0, 15))).thenReturn(calculatedValues);
        assertTrue(taxCodeValidation.taxCodeIsValid(taxCode));
    }

    @ParameterizedTest(name = "{index} => surnameCode={0}, nameCode={1}, birthCode={2}, cityCode={3}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeValidationStreamData#generateValidationCharacterNotValidValues")
    @DisplayName("Exception during generation of validation character due to wrong parameters")
    void generateValidationCharacterThrowException(String surnameCode, String nameCode, String birthCode, String cityCode) {
        assertThatThrownBy(() -> {
            taxCodeValidation.generateValidationCharacter(surnameCode, nameCode, birthCode, cityCode);
        }).isInstanceOf(IllegalArgumentException.class);
        verify(stringConverter, never()).convertStringInTaxCodeIntegerValues(anyString());
    }

    @Test
    @DisplayName("Generate validation character correctly")
    void generateValidationCharacter() {
        var taxCode = "RSSMRA85T10A562";
        var calculatedValues = new int[]{8, 18, 12, 12, 8, 0, 19, 5, 14, 1, 1, 0, 13, 6, 5};
        when(stringConverter.convertStringInTaxCodeIntegerValues(taxCode)).thenReturn(calculatedValues);
        assertEquals('S', taxCodeValidation.generateValidationCharacter("RSS", "MRA",
                "85T10", "A562"));
    }
}
