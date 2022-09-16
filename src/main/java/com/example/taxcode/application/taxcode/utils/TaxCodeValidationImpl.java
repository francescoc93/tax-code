package com.example.taxcode.application.taxcode.utils;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.TaxCodeFactory;
import com.example.taxcode.application.factory.dto.TaxCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.example.taxcode.application.utils.ASCIICodeConstants.CAPITAL_LETTER_A;


@Component
@RequiredArgsConstructor
public class TaxCodeValidationImpl implements TaxCodeValidation {
    private static final int TAX_CODE_LENGTH = 16;
    private static final int TAX_CODE_WITHOUT_VALIDATION_CHARACTER_LENGTH = 15;
    private static final String TAX_CODE_WITHOUT_VALIDATION_CHARACTER_REGEX = "[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}";
    private static final String TAX_CODE_REGEX = TAX_CODE_WITHOUT_VALIDATION_CHARACTER_REGEX + "[A-Za-z]{1}";
    private static final int NUMBER_OF_LETTERS = 26;
    private final StringConverter taxCodeConverter;

    @Override
    public char generateValidationCharacter(String surnameCode, String nameCode, String dateOfBirthCode, String cityCode) {
        var taxCode = surnameCode + nameCode + dateOfBirthCode + cityCode;

        if (!validateTaxCode(taxCode, TAX_CODE_WITHOUT_VALIDATION_CHARACTER_REGEX)) {
            throw new IllegalArgumentException("The tax code \"" + taxCode + "\" is not valid. The string should be length "
                    + TAX_CODE_WITHOUT_VALIDATION_CHARACTER_LENGTH + " characters and must contains only" +
                    "digits and letters");
        }

        var integerValues = taxCodeConverter.convertStringInTaxCodeIntegerValues(taxCode);
        return calculateValidationCharacter(integerValues);
    }

    @Override
    public boolean taxCodeIsValid(String taxCode) {
        taxCodeIsValidOrThrowException(taxCode, TAX_CODE_REGEX);

        var taxCodeWithoutValidationCharacter = taxCode.substring(0, taxCode.length() - 1);
        var validationCharacterProvided = taxCode.charAt(taxCode.length() - 1);

        var integerValues = taxCodeConverter.convertStringInTaxCodeIntegerValues(taxCodeWithoutValidationCharacter);
        var validationCharacterCalculated = calculateValidationCharacter(integerValues);

        return validationCharacterProvided == validationCharacterCalculated;
    }

    @Override
    public TaxCode parseTaxCode(String taxCode) {
        taxCodeIsValidOrThrowException(taxCode, TAX_CODE_REGEX);

        var taxCodeUpperCase = taxCode.toUpperCase();
        var surnameCode = taxCodeUpperCase.substring(0, 3);
        var nameCode = taxCodeUpperCase.substring(3, 6);
        var yearBirthCode = taxCodeUpperCase.substring(6, 8);
        var monthBirthCode = taxCodeUpperCase.substring(8, 9);
        var dayBirthCode = taxCode.substring(9, 11);
        var cityCode = taxCodeUpperCase.substring(11, 15);
        var validationCode = taxCodeUpperCase.charAt(15);
        DateBirthCode birthCode = new DateBirthCode(yearBirthCode, monthBirthCode, dayBirthCode);

        return TaxCodeFactory.makeTaxCode(surnameCode,nameCode, birthCode, cityCode, validationCode);
    }


    private char calculateValidationCharacter(int[] integerValues) {
        var sum = Arrays.stream(integerValues).sum();
        return (char) (CAPITAL_LETTER_A + (sum % NUMBER_OF_LETTERS));
    }

    private boolean validateTaxCode(String taxCode, String regexToMatch) {
        if (taxCode == null) {
            return false;
        }

        return taxCode.matches(regexToMatch);
    }


    private void taxCodeIsValidOrThrowException(String taxCode, String taxCodeRegex) {
        if (!validateTaxCode(taxCode, taxCodeRegex)) {
            throw new IllegalArgumentException("The tax code \"" + taxCode + "\" is not valid. The string should be length "
                    + TAX_CODE_LENGTH + " characters and must contains only " +
                    "digits and letters");
        }
    }

}
