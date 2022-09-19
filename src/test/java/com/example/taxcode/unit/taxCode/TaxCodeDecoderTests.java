package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.entity.CityEntity;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.exception.TaxCodeNotValidException;
import com.example.taxcode.application.repository.CityRepository;
import com.example.taxcode.application.repository.PeopleRepository;
import com.example.taxcode.application.taxcode.decode.TaxCodeDateBirthDecoder;
import com.example.taxcode.application.taxcode.decode.impl.TaxCodeDecoderImpl;
import com.example.taxcode.application.taxcode.utils.TaxCodeValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCodeDecoderTests {
    @InjectMocks
    private TaxCodeDecoderImpl taxCodeDecoder;
    @Mock
    private PeopleRepository peopleRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private TaxCodeValidation taxCodeValidation;
    @Mock
    private TaxCodeDateBirthDecoder taxCodeDateBirthDecoder;

    @DisplayName("Tax code not valid due to no match with the regex")
    @Test
    void invalidTaxCodeRegex() {
        var taxCode = "FNCMRA";
        when(taxCodeValidation.taxCodeIsValid(taxCode)).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeDecoder.decodeTaxCode(taxCode);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCode);
        verify(taxCodeValidation, never()).parseTaxCode(any());
        verify(cityRepository, never()).findById(any());
        verify(taxCodeDateBirthDecoder, never()).decodeGender(any());
        verify(taxCodeDateBirthDecoder, never()).decodeDateOfBirth(any());

    }

    @DisplayName("Tax code not valid due to no match with the validation character")
    @Test
    void invalidTaxCodeValidationCharacter() {
        var taxCode = "RSSMRA85T10A562A";
        when(taxCodeValidation.taxCodeIsValid(taxCode)).thenReturn(false);

        assertThatThrownBy(() -> {
            taxCodeDecoder.decodeTaxCode(taxCode);
        }).isInstanceOf(TaxCodeNotValidException.class);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCode);
        verify(taxCodeValidation, never()).parseTaxCode(any());
        verify(cityRepository, never()).findById(any());
        verify(taxCodeDateBirthDecoder, never()).decodeGender(any());
        verify(taxCodeDateBirthDecoder, never()).decodeDateOfBirth(any());
    }

    @DisplayName("Error while decoding the tax code due to the city code " +
            "in the tax code is not present into the database")
    @Test
    void cityNotFoundException() {
        var taxCodeString = "RSSMRA85T10A562A";
        var cityCode = "A562";
        var taxCode = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(new DateBirthCode("85", "T", "10"))
                .cityCode("A562")
                .build();

        when(taxCodeValidation.taxCodeIsValid(taxCodeString)).thenReturn(true);
        when(taxCodeValidation.parseTaxCode(taxCodeString)).thenReturn(taxCode);
        when(cityRepository.findById(cityCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            taxCodeDecoder.decodeTaxCode(taxCodeString);
        }).isInstanceOf(CityCodeNotFoundException.class);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCodeString);
        verify(taxCodeValidation, times(1)).parseTaxCode(taxCodeString);
        verify(cityRepository, times(1)).findById(cityCode);
        verify(taxCodeDateBirthDecoder, never()).decodeGender(any());
        verify(taxCodeDateBirthDecoder, never()).decodeDateOfBirth(any());
    }

    @DisplayName("Error while decoding the tax code due to an invalid birth code")
    @Test
    void notValidDateBirthCode() {
        var taxCodeString = "RSSMRA85T80A562A";
        var cityCode = "A562";
        var cityName = "ROMA";
        var city = new CityEntity(cityCode, cityName);
        var dateBirthCode = new DateBirthCode("85", "T", "80");

        var taxCode = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(dateBirthCode)
                .cityCode("A562")
                .build();

        when(taxCodeValidation.taxCodeIsValid(taxCodeString)).thenReturn(true);
        when(taxCodeValidation.parseTaxCode(taxCodeString)).thenReturn(taxCode);
        when(cityRepository.findById(cityCode)).thenReturn(Optional.of(city));
        when(taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode)).thenThrow(DateTimeException.class);

        assertThatThrownBy(() -> {
            taxCodeDecoder.decodeTaxCode(taxCodeString);
        }).isInstanceOf(DateTimeException.class);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCodeString);
        verify(taxCodeValidation, times(1)).parseTaxCode(taxCodeString);
        verify(cityRepository, times(1)).findById(cityCode);
        verify(taxCodeDateBirthDecoder, never()).decodeGender(any());
        verify(taxCodeDateBirthDecoder, times(1)).decodeDateOfBirth(any());
    }

    @DisplayName("Error while decoding the tax code due to an invalid gender code")
    @Test
    void notValidGenderCode() {
        var taxCodeString = "RSSMRA85T80A562A";
        var cityCode = "A562";
        var cityName = "ROMA";
        var city = new CityEntity(cityCode, cityName);
        var dateBirthCode = new DateBirthCode("85", "T", "10");

        var taxCode = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(dateBirthCode)
                .cityCode("A562")
                .build();

        when(taxCodeValidation.taxCodeIsValid(taxCodeString)).thenReturn(true);
        when(taxCodeValidation.parseTaxCode(taxCodeString)).thenReturn(taxCode);
        when(cityRepository.findById(cityCode)).thenReturn(Optional.of(city));
        when(taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode)).thenReturn(any());
        when(taxCodeDateBirthDecoder.decodeGender(dateBirthCode)).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeDecoder.decodeTaxCode(taxCodeString);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCodeString);
        verify(taxCodeValidation, times(1)).parseTaxCode(taxCodeString);
        verify(cityRepository, times(1)).findById(cityCode);
        verify(taxCodeDateBirthDecoder, times(1)).decodeGender(any());
        verify(taxCodeDateBirthDecoder, times(1)).decodeDateOfBirth(any());

    }

    @DisplayName("Tax code decoded successfully")
    @Test
    void taxCodeDecodedSuccessfully() throws CityCodeNotFoundException, TaxCodeNotValidException {
        var taxCodeString = "RSSMRA85T31A562S";
        var cityCode = "A562";
        var cityName = "ROMA";
        var gender = Gender.MAN;
        var city = new CityEntity(cityCode, cityName);
        var dateBirthCode = new DateBirthCode("85", "T", "31");
        var birthDate = LocalDate.of(1985, Month.DECEMBER, 31);
        var taxCode = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(dateBirthCode)
                .cityCode("A562")
                .build();
        var name = new ArrayList<String>();
        var surname = new ArrayList<String>();
        name.add("MARIO");
        surname.add("ROSSI");
        surname.add("ROSSO");
        surname.add("ROSSA");

        var taxCodeDecode = TaxCodeDecode.builder()
                .names(name)
                .surnames(surname)
                .dateOfBirth(birthDate)
                .placeOfBirth(cityName)
                .gender(gender)
                .build();

        when(taxCodeValidation.taxCodeIsValid(taxCodeString)).thenReturn(true);
        when(taxCodeValidation.parseTaxCode(taxCodeString)).thenReturn(taxCode);
        when(cityRepository.findById(cityCode)).thenReturn(Optional.of(city));
        when(taxCodeDateBirthDecoder.decodeDateOfBirth(dateBirthCode)).thenReturn(birthDate);
        when(taxCodeDateBirthDecoder.decodeGender(dateBirthCode)).thenReturn(gender);
        when(peopleRepository.findDistinctSurnames(taxCode.getSurnameCode())).thenReturn(surname);
        when(peopleRepository.findDistinctNames(taxCode.getNameCode(),gender)).thenReturn(name);

        var returnTaxCodeDecoded = taxCodeDecoder.decodeTaxCode(taxCodeString);

        verify(taxCodeValidation, times(1)).taxCodeIsValid(taxCodeString);
        verify(taxCodeValidation, times(1)).parseTaxCode(taxCodeString);
        verify(cityRepository, times(1)).findById(cityCode);
        verify(taxCodeDateBirthDecoder, times(1)).decodeGender(any());
        verify(taxCodeDateBirthDecoder, times(1)).decodeDateOfBirth(any());

        assertThat(taxCodeDecode).usingRecursiveComparison().isEqualTo(returnTaxCodeDecoded);
    }

}
