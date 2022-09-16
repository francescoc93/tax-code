package com.example.taxcode.unit.serivce;

import com.example.taxcode.application.entity.PersonEntity;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.repository.PersonRepository;
import com.example.taxcode.application.service.impl.GenerateTaxCodeServiceImpl;
import com.example.taxcode.application.taxcode.generator.TaxCodeGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateTaxCodeServiceTests {
    private static Person person;
    private static Person personNormalized;
    private static PersonEntity personEntity;
    private static TaxCode taxCode;
    private static String taxCodeStringFormat;
    @InjectMocks
    private GenerateTaxCodeServiceImpl generateTaxCodeService;
    @Mock
    private TaxCodeGenerator taxCodeGenerator;
    @Mock
    PersonRepository personRepository;


    @BeforeAll
    static void initializeData() {
        var name = "Mario";
        var nameNormalized = "MARIO";
        var surname = "Rossi";
        var surnameNormalized = "ROSSI";
        var gender = Gender.MALE;
        var placeOfBirth = "  Ascoli  Piceno  ";
        var placeOfBirthNormalized = "ASCOLI PICENO";
        var dateOfBirth = LocalDate.of(1990,Month.JANUARY,10);
        var nameCode = "MRA";
        var surnameCode = "RSS";
        var cityCode = "A562";
        var validationCharacter = 'I';
        var dateBirthCode = new DateBirthCode("90","A","10");

        taxCodeStringFormat = surnameCode+nameCode+dateBirthCode.toString()+cityCode+validationCharacter;

        person = new Person(name,surname,gender,placeOfBirth,dateOfBirth);
        personNormalized = new Person(nameNormalized,surnameNormalized,gender,placeOfBirthNormalized,dateOfBirth);
        personEntity = PersonEntity.builder()
                .nameTaxCode(nameCode)
                .cityTaxCode(cityCode)
                .surnameTaxCode(surnameCode)
                .dateOfBirthTaxCode("90A10")
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .name(nameNormalized)
                .surname(surnameNormalized)
                .placeOfBirth(placeOfBirthNormalized)
                .validationCharacterTaxCode(validationCharacter)
                .build();
        taxCode = TaxCode.builder()
                .surnameCode(surnameCode)
                .nameCode(nameCode)
                .dateOfBirthCode(dateBirthCode)
                .cityCode(cityCode)
                .validationCode(validationCharacter)
                .build();
    }

    @DisplayName("Find tax code on database")
    @Test
    void findTaxCodeOnDatabase() throws CityCodeNotFoundException {
        when(personRepository.findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth())).
                thenReturn(Optional.of(personEntity));

        var taxCodeResponse = generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person);

        assertEquals(taxCodeStringFormat,taxCodeResponse.getTaxCode());
        verify(taxCodeGenerator,never()).generateTaxCode(personNormalized);
        verify(personRepository,never()).save(personEntity);
        verify(personRepository,times(1)).findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth());
    }

    @DisplayName("Generate tax code")
    @Test
    void generateTaxCode() throws CityCodeNotFoundException {
        when(personRepository.findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth())).
                thenReturn(Optional.empty());

        when(taxCodeGenerator.generateTaxCode(any())).thenReturn(taxCode);

        var taxCodeResponse = generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person);

        assertEquals(taxCodeStringFormat,taxCodeResponse.getTaxCode());
        verify(taxCodeGenerator,times(1)).generateTaxCode(any());
        verify(personRepository,times(1)).save(any());
        verify(personRepository,times(1)).findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth());
    }

    @DisplayName("Generate tax code throw IllegalArgumentException")
    @Test
    void generateTaxCodeThrowIllegalArgumentException() throws CityCodeNotFoundException {
        when(personRepository.findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth())).
                thenReturn(Optional.empty());

        when(taxCodeGenerator.generateTaxCode(any())).thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person);
        }).isInstanceOf(IllegalArgumentException.class);

        verify(taxCodeGenerator,times(1)).generateTaxCode(any());
        verify(personRepository,never()).save(any());
        verify(personRepository,times(1)).findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth());
    }

    @DisplayName("Generate tax code throw CityCodeNotFoundException")
    @Test
    void generateTaxCodeThrowCityCodeNotFoundException() throws CityCodeNotFoundException {
        when(personRepository.findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth())).
                thenReturn(Optional.empty());

        when(taxCodeGenerator.generateTaxCode(any())).thenThrow(CityCodeNotFoundException.class);

        assertThatThrownBy(() -> {
            generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person);
        }).isInstanceOf(CityCodeNotFoundException.class);

        verify(taxCodeGenerator,times(1)).generateTaxCode(any());
        verify(personRepository,never()).save(any());
        verify(personRepository,times(1)).findTaxCodeByPersonInformation(personNormalized.getName(),
                personNormalized.getSurname(),
                personNormalized.getGender(),
                personNormalized.getPlaceOfBirth(),
                personNormalized.getDateOfBirth());
    }

}
