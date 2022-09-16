package com.example.taxcode.application.service.impl;

import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.TaxCodeFactory;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import com.example.taxcode.application.entity.PersonEntity;
import com.example.taxcode.application.repository.PersonRepository;
import com.example.taxcode.application.service.GenerateTaxCodeService;
import com.example.taxcode.application.taxcode.generator.TaxCodeGenerator;
import com.example.taxcode.application.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GenerateTaxCodeServiceImpl implements GenerateTaxCodeService {
    private final TaxCodeGenerator taxCodeGenerator;
    private final PersonRepository personRepository;

    @Override
    public TaxCodeResponse retrieveFromDatabaseOrGenerateTaxCode(Person person){
        var personNormalized = normalizePersonData(person);
        return findTaxCodeOnRepository(personNormalized).map(taxCode -> new TaxCodeResponse(taxCode.toString()))
                .orElseGet(() -> generateAndSaveTaxCode(personNormalized));
    }

    private TaxCodeResponse generateAndSaveTaxCode(Person personNormalized) {
        TaxCode taxCode = taxCodeGenerator.generateTaxCode(personNormalized);
        savePersonAndTaxCode(personNormalized, taxCode);

        return new TaxCodeResponse(taxCode.toString());
    }

    private Person normalizePersonData(Person person) {
        var name = StringUtils.normalizeSpace(person.getName()).toUpperCase();
        var surname = StringUtils.normalizeSpace(person.getSurname()).toUpperCase();
        var placeOfBirth = StringUtils.normalizeSpace(person.getPlaceOfBirth()).toUpperCase();

        return new Person(name, surname, person.getGender(), placeOfBirth, person.getDateOfBirth());
    }

    private void savePersonAndTaxCode(Person person, TaxCode taxCode) {

        PersonEntity personEntity = PersonEntity.builder()
                .nameTaxCode(taxCode.getNameCode())
                .surnameTaxCode(taxCode.getSurnameCode())
                .dateOfBirthTaxCode(taxCode.getDateOfBirthCode().toString())
                .cityTaxCode(taxCode.getCityCode())
                .validationCharacterTaxCode(taxCode.getValidationCode())
                .name(person.getName())
                .surname(person.getSurname())
                .gender(person.getGender())
                .placeOfBirth(person.getPlaceOfBirth())
                .dateOfBirth(person.getDateOfBirth())
                .build();

        personRepository.save(personEntity);
    }


    private Optional<TaxCode> findTaxCodeOnRepository(Person person) {
        var personOptional = personRepository.findTaxCodeByPersonInformation(
                person.getName(),
                person.getSurname(),
                person.getGender(),
                person.getPlaceOfBirth(),
                person.getDateOfBirth());

        return personOptional.map(this::buildTaxCodeFromPersonEntity);
    }

    private TaxCode buildTaxCodeFromPersonEntity(PersonEntity person) {
        var dateBirthTaxCode = person.getDateOfBirthTaxCode();
        var surnameTaxCode = person.getSurnameTaxCode();
        var nameTaxCode = person.getNameTaxCode();
        var cityTaxCode = person.getCityTaxCode();
        var validationCharacterTaxCode = person.getValidationCharacterTaxCode();

        return TaxCodeFactory.makeTaxCode(surnameTaxCode, nameTaxCode, dateBirthTaxCode, cityTaxCode, validationCharacterTaxCode);
    }
}
