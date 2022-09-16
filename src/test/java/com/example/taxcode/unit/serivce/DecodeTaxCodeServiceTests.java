package com.example.taxcode.unit.serivce;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.exception.TaxCodeNotValidException;
import com.example.taxcode.application.service.impl.DecodeTaxCodeServiceImpl;
import com.example.taxcode.application.taxcode.decode.TaxCodeDecoder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecodeTaxCodeServiceTests {
    @InjectMocks
    private DecodeTaxCodeServiceImpl decodeTaxCodeService;
    @Mock
    private TaxCodeDecoder taxCodeDecoder;

    @BeforeAll
    static void initializeData(){

    }

    @SneakyThrows
    @Test
    @DisplayName("Decode tax code successfully")
    void decodeTaxCode(){
        var taxCodeString = "RSSMRA85B02A562I";
        var name = new ArrayList<String>();
        var surname = new ArrayList<String>();
        name.add("MARIO");
        surname.add("ROSSI");
        surname.add("ROSSO");
        surname.add("ROSSA");

        var taxCodeDecode = TaxCodeDecode.builder()
                .placeOfBirth("ROMA")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1985, Month.FEBRUARY,2))
                .names(name)
                .surnames(surname)
                .build();
        when(taxCodeDecoder.decodeTaxCode(taxCodeString)).thenReturn(taxCodeDecode);

        var taxCodeDecodeReturned = decodeTaxCodeService.decodeTaxCode(taxCodeString);

        verify(taxCodeDecoder,times(1)).decodeTaxCode(taxCodeString);

        assertThat(taxCodeDecode).usingRecursiveComparison().isEqualTo(taxCodeDecodeReturned);
    }

    @SneakyThrows
    @Test
    @DisplayName("Decode tax code throw IllegalArgumentException")
    void decodeTaxCodeThrowIllegalArgumentException(){
        var taxCodeString = "RSSM89234C084I";
        when(taxCodeDecoder.decodeTaxCode(taxCodeString)).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            decodeTaxCodeService.decodeTaxCode(taxCodeString);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeDecoder,times(1)).decodeTaxCode(taxCodeString);
    }

    @SneakyThrows
    @Test
    @DisplayName("Decode tax code throw CityNotFoundException")
    void decodeTaxCodeThrowCityNotFoundException(){
        var taxCodeString = "RSSMRA85A10A000A";
        when(taxCodeDecoder.decodeTaxCode(taxCodeString)).thenThrow(CityCodeNotFoundException.class);

        assertThatThrownBy(() -> {
            decodeTaxCodeService.decodeTaxCode(taxCodeString);
        }).isInstanceOf(CityCodeNotFoundException.class);

        verify(taxCodeDecoder,times(1)).decodeTaxCode(taxCodeString);
    }

    @SneakyThrows
    @Test
    @DisplayName("Decode tax code throw TaxCodeNotValidException")
    void decodeTaxCodeThrowTaxCodeNotValidException(){
        var taxCodeString = "RSSMRA85A10A562A";
        when(taxCodeDecoder.decodeTaxCode(taxCodeString)).thenThrow(TaxCodeNotValidException.class);

        assertThatThrownBy(() -> {
            decodeTaxCodeService.decodeTaxCode(taxCodeString);
        }).isInstanceOf(TaxCodeNotValidException.class);

        verify(taxCodeDecoder,times(1)).decodeTaxCode(taxCodeString);
    }
}
