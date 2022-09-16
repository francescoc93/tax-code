package com.example.taxcode.application.taxcode.utils;

import com.example.taxcode.application.factory.dto.TaxCode;

public interface TaxCodeValidation {
    char generateValidationCharacter(String surnameCode, String nameCode, String dateOfBirthCode, String cityCode);
    boolean taxCodeIsValid(String taxCode);
    TaxCode parseTaxCode(String taxCode);
}
