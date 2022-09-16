package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.Person;
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
    private static final Gender GENDER = Gender.MALE;
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
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeGeneratorStreamData#invalidPerson")
    void invalidInput(Person person) throws CityCodeNotFoundException {
        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
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
        var person = new Person(NAME, SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenThrow(CityCodeNotFoundException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
        }).isInstanceOf(CityCodeNotFoundException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(person.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(person.getName());
        verify(taxCodeNameSurnameGenerator, never()).calculateSurnameCode(person.getSurname());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());

    }

    @DisplayName("Generation tax code throw exception due to place of birth is not valid")
    @Test
    void placeOfBirthNotValid() throws CityCodeNotFoundException {
        var person = new Person(NAME, SURNAME, GENDER, " ", DATE_OF_BIRTH);
        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(person.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(person.getName());
        verify(taxCodeNameSurnameGenerator, never()).calculateSurnameCode(person.getSurname());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());

    }

    @DisplayName("Generation tax code throw exception due to surname is not valid")
    @Test
    void surnameNotValid() throws CityCodeNotFoundException {
        var person = new Person(NAME, " ", GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenReturn("A562");
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(person.getSurname())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(person.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(person.getSurname());
        verify(taxCodeNameSurnameGenerator, never()).calculateNameCode(person.getName());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Generation tax code throw exception due to name is not valid")
    @Test
    void nameNotValid() throws CityCodeNotFoundException {
        var person = new Person(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);

        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenReturn("A562");
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(person.getSurname())).thenReturn("RSS");
        when(taxCodeNameSurnameGenerator.calculateNameCode(person.getName())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(person.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(person.getSurname());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateNameCode(person.getName());
        verify(taxCodeDateBirthGenerator, never()).calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        verify(taxCodeValidation, never()).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Generation tax code throw exception due to the parameter provided for the " +
            "generation of the validation character are not valid")
    void generationValidationCharacterThrowException() throws CityCodeNotFoundException {
        var person = new Person(" ", SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);
        var nameCode = "MRA";
        var surnameCode = "RSS";
        var cityCode = "A562";
        var dateBirthCode = new DateBirthCode("90", "L", "12");

        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenReturn(cityCode);
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(person.getSurname())).thenReturn(surnameCode);
        when(taxCodeNameSurnameGenerator.calculateNameCode(person.getName())).thenReturn(nameCode);
        when(taxCodeDateBirthGenerator.calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender()))
                .thenReturn(dateBirthCode);
        when(taxCodeValidation.generateValidationCharacter(surnameCode, nameCode, dateBirthCode.toString(), cityCode))
                .thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            taxCodeGenerator.generateTaxCode(person);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeCityCodeGenerator, times(1)).calculateCityCode(person.getPlaceOfBirth());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateSurnameCode(person.getSurname());
        verify(taxCodeNameSurnameGenerator, times(1)).calculateNameCode(person.getName());
        verify(taxCodeDateBirthGenerator, times(1)).calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        verify(taxCodeValidation, times(1)).generateValidationCharacter(anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Generation tax code")
    @Test
    void generateTaxCode() throws CityCodeNotFoundException {
        var person = new Person(NAME, SURNAME, GENDER, PLACE_OF_BIRTH, DATE_OF_BIRTH);
        var taxCodeExpected = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(new DateBirthCode("90", "L", "12"))
                .cityCode("A562")
                .build();

        when(taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth())).thenReturn(taxCodeExpected.getCityCode());
        when(taxCodeNameSurnameGenerator.calculateSurnameCode(person.getSurname())).thenReturn(taxCodeExpected.getSurnameCode());
        when(taxCodeNameSurnameGenerator.calculateNameCode(person.getName())).thenReturn(taxCodeExpected.getNameCode());
        when(taxCodeDateBirthGenerator.calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender()))
                .thenReturn(taxCodeExpected.getDateOfBirthCode());
        when(taxCodeValidation.generateValidationCharacter(taxCodeExpected.getSurnameCode(),
                taxCodeExpected.getNameCode(),
                "90L12",
                taxCodeExpected.getCityCode()))
                .thenReturn(taxCodeExpected.getValidationCode());

        var taxCodeReturned = taxCodeGenerator.generateTaxCode(person);

        assertThat(taxCodeExpected).usingRecursiveComparison().isEqualTo(taxCodeReturned);
    }
}
