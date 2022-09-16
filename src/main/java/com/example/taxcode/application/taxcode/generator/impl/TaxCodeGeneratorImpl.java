package com.example.taxcode.application.taxcode.generator.impl;

import com.example.taxcode.application.factory.TaxCodeFactory;
import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.dto.TaxCode;
import com.example.taxcode.application.taxcode.generator.TaxCodeCityCodeGenerator;
import com.example.taxcode.application.taxcode.generator.TaxCodeDateBirthGenerator;
import com.example.taxcode.application.taxcode.generator.TaxCodeGenerator;
import com.example.taxcode.application.taxcode.generator.TaxCodeNameSurnameGenerator;
import com.example.taxcode.application.taxcode.utils.TaxCodeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class TaxCodeGeneratorImpl implements TaxCodeGenerator {

    private final TaxCodeValidation taxCodeValidation;
    private final TaxCodeNameSurnameGenerator taxCodeNameSurnameGenerator;
    private final TaxCodeDateBirthGenerator taxCodeDateBirthGenerator;
    private final TaxCodeCityCodeGenerator taxCodeCityCodeGenerator;

    @Override
    public TaxCode generateTaxCode(Person person) {
        if (!validate(person)) {
            throw new IllegalArgumentException("person is null or it contains some null values");
        }

        var cityCode = taxCodeCityCodeGenerator.calculateCityCode(person.getPlaceOfBirth());
        var surnameCode = taxCodeNameSurnameGenerator.calculateSurnameCode(person.getSurname());
        var nameCode = taxCodeNameSurnameGenerator.calculateNameCode(person.getName());
        var dateOfBirthCode = taxCodeDateBirthGenerator.calculateDateOfBirthCode(person.getDateOfBirth(), person.getGender());
        var validationCode = taxCodeValidation.generateValidationCharacter(surnameCode, nameCode, dateOfBirthCode.toString(), cityCode);

        return TaxCodeFactory.makeTaxCode(surnameCode, nameCode, dateOfBirthCode, cityCode, validationCode);
    }

    private boolean validate(Person person) {
        return person != null && person.getName() != null && person.getSurname() != null && person.getGender() != null
                && person.getPlaceOfBirth() != null && person.getDateOfBirth() != null;
    }

}
