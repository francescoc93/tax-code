package com.example.taxcode.unit.controller;

import com.example.taxcode.application.controller.TaxCodeController;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import com.example.taxcode.application.service.DecodeTaxCodeService;
import com.example.taxcode.application.service.GenerateTaxCodeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCodeControllerTests {

    @Mock
    private DecodeTaxCodeService decodeTaxCodeService;

    @Mock
    private GenerateTaxCodeService generateTaxCodeService;

    @InjectMocks
    private TaxCodeController controller;

    @SneakyThrows
    @Test
    @DisplayName("Generate tax code successfully")
    void generateTaxCodeSuccessfully() {
        var taxCodeResponse = new TaxCodeResponse("RSSMRA85B02A562S");
        var person = new People("Mario", "Rossi", Gender.MAN, "Roma", LocalDate.of(1980, Month.FEBRUARY, 2));

        when(generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person)).thenReturn(taxCodeResponse);
        var taxCodeResponseActual = controller.calculateTaxCode(person);
        assertEquals(taxCodeResponse.getTaxCode(), taxCodeResponseActual.getTaxCode());
        verify(generateTaxCodeService, times(1)).retrieveFromDatabaseOrGenerateTaxCode(person);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index} => person={0}, exceptionClass={1}")
    @MethodSource("com.example.taxcode.unit.controller.stream.TaxCodeControllerStreamData#provideInputWithExceptionClass")
    @DisplayName("Generate tax code throw exception")
    void generateTaxCodeThrowException(People people, Class exceptionClass) {
        when(generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(people)).thenThrow(exceptionClass);

        assertThatThrownBy(() -> {
            controller.calculateTaxCode(people);
        }).isInstanceOf(exceptionClass);

        verify(generateTaxCodeService, times(1)).retrieveFromDatabaseOrGenerateTaxCode(people);
    }

    @SneakyThrows
    @Test
    @DisplayName("Decode tax code successfully")
    void decodeTaxCodeSuccessfully() {
        var taxCodeToDecode = "RSSMRA85B02A562S";
        var name = new ArrayList<String>();
        var surname = new ArrayList<String>();

        name.add("MARIO");
        surname.add("ROSSI");
        surname.add("ROSSO");
        surname.add("ROSSA");

        var taxCodeDecode = TaxCodeDecode.builder()
                .names(name)
                .surnames(surname)
                .dateOfBirth(LocalDate.of(1985, Month.FEBRUARY, 2))
                .placeOfBirth("ROMA")
                .gender(Gender.MAN)
                .build();

        when(decodeTaxCodeService.decodeTaxCode(taxCodeToDecode)).thenReturn(taxCodeDecode);

        var taxCodeDecodeActual = controller.decodeTaxCode(taxCodeToDecode);

        verify(decodeTaxCodeService, times(1)).decodeTaxCode(taxCodeToDecode);
        assertThat(taxCodeDecode).usingRecursiveComparison().isEqualTo(taxCodeDecodeActual);
    }

    @SneakyThrows
    @ParameterizedTest(name = "{index} => taxCode={0}, exceptionClass={1}")
    @MethodSource("com.example.taxcode.unit.controller.stream.TaxCodeControllerStreamData#provideDecodeTaxInputWithExceptionClass")
    @DisplayName("Decode tax code throw exception")
    void decodeTaxCodeThrowException(String taxCode, Class exceptionClass) {
        when(decodeTaxCodeService.decodeTaxCode(taxCode)).thenThrow(exceptionClass);

        assertThatThrownBy(() -> {
            controller.decodeTaxCode(taxCode);
        }).isInstanceOf(exceptionClass);

        verify(decodeTaxCodeService, times(1)).decodeTaxCode(taxCode);
    }

}
