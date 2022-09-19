package com.example.taxcode.application.taxcode.generator.impl;

import com.example.taxcode.application.factory.TaxCodeFactory;
import com.example.taxcode.application.dto.People;
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
    public TaxCode generateTaxCode(People people) {
        if (!validate(people)) {
            throw new IllegalArgumentException("person is null or it contains some null values");
        }

        var cityCode = taxCodeCityCodeGenerator.calculateCityCode(people.getPlaceOfBirth());
        var surnameCode = taxCodeNameSurnameGenerator.calculateSurnameCode(people.getSurname());
        var nameCode = taxCodeNameSurnameGenerator.calculateNameCode(people.getName());
        var dateOfBirthCode = taxCodeDateBirthGenerator.calculateDateOfBirthCode(people.getDateOfBirth(), people.getGender());
        var validationCode = taxCodeValidation.generateValidationCharacter(surnameCode, nameCode, dateOfBirthCode.toString(), cityCode);

        return TaxCodeFactory.makeTaxCode(surnameCode, nameCode, dateOfBirthCode, cityCode, validationCode);
    }

    private boolean validate(People people) {
        return people != null && people.getName() != null && people.getSurname() != null && people.getGender() != null
                && people.getPlaceOfBirth() != null && people.getDateOfBirth() != null;
    }

}
