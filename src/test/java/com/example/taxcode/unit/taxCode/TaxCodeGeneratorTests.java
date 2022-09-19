package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.taxcode.generator.TaxCodeCityCodeGenerator;
import com.example.taxcode.application.taxcode.generator.TaxCodeDateBirthGenerator;
import com.example.taxcode.application.taxcode.generator.impl.TaxCodeGeneratorImpl;
import com.example.taxcode.application.taxcode.generator.TaxCodeNameSurnameGenerator;
import com.example.taxcode.application.taxcode.utils.TaxCodeValidation;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCodeGeneratorTests {
    private static final String NAME = "Mario";
    private static final String SURNAME = "Rossi";
    private static final String PLACE_OF_BIRTH = "Roma";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, Month.JULY, 12);
    private static final Gender GENDER = Gender.MAN;
    @InjectMocks
    private TaxCodeGeneratorImpl taxCodeGenerator;
    @Mock
    private TaxCodeValidation taxCodeValidation;
    @Mock
    private TaxCodeNameSurnameGenerator taxCodeNameSurnameGenerator;
    @Mock
    private TaxCodeDateBirthGenerator taxCodeDateBirthGenerator;
    @Mock
    private TaxCodeCityCodeGenerator taxCodeCityCodeGenerator;

    @DisplayName("Invalid input throw exception")
    @ParameterizedTest
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeGeneratorStreamData#invalidPeople")
    void invalidInput(People people) throws CityCodeNotFoundException {
        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, never()).calculateCityCode(anyString());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(anyString());
        verify(taxCodeNameSurnameGenerator, never()).calculateSurnameCode(anyString());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(any(), any());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Generation tax code throw exception due to city code exception")
    @Test
    void cityCodeNotFoundException() throws CityCodeNotFoundException {
        var people = new People(NAME, SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenThrow(CityCodeNotFoundException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(CityCodeNotFoundException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(people.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(people.getName());
        verify(taxCodeNameSurnameGenerator, never()).calculateSurnameCode(people.getSurname());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());

    }

    @DisplayName("Generation tax code throw exception due to place of birth is not valid")
    @Test
    void placeOfBirthNotValid() throws CityCodeNotFoundException {
        var people = new People(NAME, SURNAME, GENDER, " ", DATE_OF_BIRTH);
        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(people.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(people.getName());
        verify(taxCodeNameSurnameGenerator, never()).calculateSurnameCode(people.getSurname());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());

    }

    @DisplayName("Generation tax code throw exception due to surname is not valid")
    @Test
    void surnameNotValid() throws CityCodeNotFoundException {
        var people = new People(NAME, " ", GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenReturn("A562");
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(people.getSurname())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(people.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(people.getSurname());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(people.getName());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Generation tax code throw exception due to name is not valid")
    @Test
    void nameNotValid() throws CityCodeNotFoundException {
        var people = new People(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenReturn("A562");
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(people.getSurname())).thenReturn("RSS");
        when(taxCodeNameSurnameGenerator.calculateNameCode(people.getName())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(people.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(people.getSurname());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateNameCode(people.getName());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Generation tax code throw exception due to the parameter provided for the " +
            "generation of the validation character are not valid")
    void generationValidationCharacterThrowException() throws CityCodeNotFoundException {
        var people = new People(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);
        var nameCode = "MRA";
        var surnameCode = "RSS";
        var cityCode = "A562";
        var dateBirthCode = new DateBirthCode("90", "L", "12");

        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenReturn(cityCode);
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(people.getSurname())).thenReturn(surnameCode);
        when(taxCodeNameSurnameGenerator.calculateNameCode(people.getName())).thenReturn(nameCode);
        when(taxCodeDateBirthGenerator.calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender()))
                .thenReturn(dateBirthCode);
        when(taxCodeValidation.generateValidationCharacter(surnameCode, nameCode, dateBirthCode.toString(), cityCode))
                .thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(people);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(people.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(people.getSurname());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateNameCode(people.getName());
        verify(taxCodeDateBirthGenerator, times(1)).calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        verify(taxCodeValidation, times(1)).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Generation tax code")
    @Test
    void generateTaxCode() throws CityCodeNotFoundException {
        var people = new People(NAME, SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);
        var taxCodeExpected = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(new DateBirthCode("90", "L", "12"))
                .cityCode("A562")
                .build();

        when(taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth())).thenReturn(taxCodeExpected.getCityCode());
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(people.getSurname())).thenReturn(taxCodeExpected.getSurnameCode());
        when(taxCodeNameSurnameGenerator.calculateNameCode(people.getName())).thenReturn(taxCodeExpected.getNameCode());
        when(taxCodeDateBirthGenerator.calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender()))
                .thenReturn(taxCodeExpected.getDateOfBirthCode());
        when(taxCodeValidation.generateValidationCharacter(taxCodeExpected.getSurnameCode(),
                taxCodeExpected.getNameCode(),
                "90L12",
                taxCodeExpected.getCityCode()))
                .thenReturn(taxCodeExpected.getValidationCode());

        var taxCodeReturned = taxCodeGenerator.generateTaxCode(people);

        assertThat(taxCodeExpected).usingRecursiveComparison().isEqualTo(taxCodeReturned);
    }
}
