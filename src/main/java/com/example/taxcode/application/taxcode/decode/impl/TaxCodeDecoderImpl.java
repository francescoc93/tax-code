package com.example.taxcode.application.taxcode.decode.impl;

import com.example.taxcode.application.factory.TaxCodeDecodeFactory;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.exception.TaxCodeNotValidException;
import com.example.taxcode.application.repository.CityRepository;
import com.example.taxcode.application.repository.PeopleRepository;
import com.example.taxcode.application.taxcode.decode.TaxCodeDateBirthDecoder;
import com.example.taxcode.application.taxcode.decode.TaxCodeDecoder;
import com.example.taxcode.application.taxcode.utils.TaxCodeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaxCodeDecoderImpl implements TaxCodeDecoder {
    private final TaxCodeValidation taxCodeValidation;
    private final PeopleRepository peopleRepository;
    private final CityRepository cityRepository;
    private final TaxCodeDateBirthDecoder taxCodeDateBirthDecoder;

    @Override
    public TaxCodeDecode decodeTaxCode(String taxCodeString) {
        if (!taxCodeValidation.taxCodeIsValid(taxCodeString)) {
            throw new TaxCodeNotValidException("The tax code '" + taxCodeString + "' is not valid.");
        }

        var taxCode = taxCodeValidation.parseTaxCode(taxCodeString);

        var placeOfBirth = decodePlaceOfBirth(taxCode.getCityCode());
        var dateOfBirth = taxCodeDateBirthDecoder.decodeDateOfBirth(taxCode.getDateOfBirthCode());
        var gender = taxCodeDateBirthDecoder.decodeGender(taxCode.getDateOfBirthCode());
        var nameList = retrieveListOfPossibleNames(taxCode.getNameCode(), gender);
        var surnameList = retrieveListOfPossibleSurnames(taxCode.getSurnameCode());

        return TaxCodeDecodeFactory.makeTaxCodeDecode(nameList, surnameList, gender, placeOfBirth, dateOfBirth);
    }


    private List<String> retrieveListOfPossibleSurnames(String surnameCode) {
        return peopleRepository.findDistinctSurnames(surnameCode.toUpperCase());
    }

    private List<String> retrieveListOfPossibleNames(String nameCode, Gender gender) {
        return peopleRepository.findDistinctNames(nameCode.toUpperCase(), gender);
    }

    private String decodePlaceOfBirth(String cityCode) {
        return cityRepository.findById(cityCode.toUpperCase())
                .orElseThrow(() -> new CityCodeNotFoundException("City not found for the city code: '" + cityCode + "'"))
                .getCity();
    }

}
