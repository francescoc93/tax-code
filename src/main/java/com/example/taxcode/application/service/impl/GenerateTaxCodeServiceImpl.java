package com.example.taxcode.application.service.impl;

import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.factory.TaxCodeFactory;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import com.example.taxcode.application.entity.PeopleEntity;
import com.example.taxcode.application.repository.PeopleRepository;
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
    private final PeopleRepository peopleRepository;

    @Override
    public TaxCodeResponse retrieveFromDatabaseOrGenerateTaxCode(People people){
        var peopleNormalized = normalizePeopleData(people);
        return findTaxCodeOnRepository(peopleNormalized).map(taxCode -> new TaxCodeResponse(taxCode.toString()))
                .orElseGet(() -> generateAndSaveTaxCode(peopleNormalized));
    }

    private TaxCodeResponse generateAndSaveTaxCode(People peopleNormalized) {
        TaxCode taxCode = taxCodeGenerator.generateTaxCode(peopleNormalized);
        savePeopleAndTaxCode(peopleNormalized, taxCode);

        return new TaxCodeResponse(taxCode.toString());
    }

    private People normalizePeopleData(People people) {
        var name = StringUtils.normalizeSpace(people.getName()).toUpperCase();
        var surname = StringUtils.normalizeSpace(people.getSurname()).toUpperCase();
        var placeOfBirth = StringUtils.normalizeSpace(people.getPlaceOfBirth()).toUpperCase();

        return new People(name, surname, people.getGender(), placeOfBirth, people.getDateOfBirth());
    }

    private void savePeopleAndTaxCode(People people, TaxCode taxCode) {

        PeopleEntity peopleEntity = PeopleEntity.builder()
                .nameTaxCode(taxCode.getNameCode())
                .surnameTaxCode(taxCode.getSurnameCode())
                .dateOfBirthTaxCode(taxCode.getDateOfBirthCode().toString())
                .cityTaxCode(taxCode.getCityCode())
                .validationCharacterTaxCode(taxCode.getValidationCode())
                .name(people.getName())
                .surname(people.getSurname())
                .gender(people.getGender())
                .placeOfBirth(people.getPlaceOfBirth())
                .dateOfBirth(people.getDateOfBirth())
                .build();

        peopleRepository.save(peopleEntity);
    }


    private Optional<TaxCode> findTaxCodeOnRepository(People people) {
        var peopleOptional = peopleRepository.findTaxCodeByPeopleInformation(
                people.getName(),
                people.getSurname(),
                people.getGender(),
                people.getPlaceOfBirth(),
                people.getDateOfBirth());

        return peopleOptional.map(this::buildTaxCodeFromPeopleEntity);
    }

    private TaxCode buildTaxCodeFromPeopleEntity(PeopleEntity people) {
        var dateBirthTaxCode = people.getDateOfBirthTaxCode();
        var surnameTaxCode = people.getSurnameTaxCode();
        var nameTaxCode = people.getNameTaxCode();
        var cityTaxCode = people.getCityTaxCode();
        var validationCharacterTaxCode = people.getValidationCharacterTaxCode();

        return TaxCodeFactory.makeTaxCode(surnameTaxCode, nameTaxCode, dateBirthTaxCode, cityTaxCode, validationCharacterTaxCode);
    }
}
