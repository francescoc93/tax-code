package com.example.taxcode.application.factory;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.TaxCode;

public class TaxCodeFactory {
    private TaxCodeFactory() {
    }

    public static TaxCode makeTaxCode(String surnameCode, String nameCode, DateBirthCode dateOfBirthCode
            , String cityCode, char validationCode) {

        return TaxCode.builder()
                .surnameCode(surnameCode)
                .nameCode(nameCode)
                .dateOfBirthCode(dateOfBirthCode)
                .cityCode(cityCode)
                .validationCode(validationCode)
                .build();
    }

    public static TaxCode makeTaxCode(String surnameCode, String nameCode, String dateOfBirthCode, String cityCode, char validationCode) {

        if(!validateDateOfBirthCode(dateOfBirthCode)){
            throw new IllegalArgumentException("dateOfBirthCode is not valid. It should be length 5 characters");
        }

        var yearOfBirthCode = dateOfBirthCode.substring(0,2);
        var monthOfBirthCode = dateOfBirthCode.substring(2,3);
        var dayOfBirthCode = dateOfBirthCode.substring(3);

        return TaxCode.builder()
                .surnameCode(surnameCode)
                .nameCode(nameCode)
                .dateOfBirthCode(new DateBirthCode(yearOfBirthCode, monthOfBirthCode, dayOfBirthCode))
                .cityCode(cityCode)
                .validationCode(validationCode)
                .build();
    }

    private static boolean validateDateOfBirthCode(String dateOfBirthCode) {
        if (dateOfBirthCode == null || dateOfBirthCode.isBlank()) {
            return false;
        }

        return dateOfBirthCode.length() == 5;
    }
}
