package com.example.taxcode.application.factory.dto;

import com.example.taxcode.application.dto.DateBirthCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaxCode {
    private final String surnameCode;
    private final String nameCode;
    private final DateBirthCode dateOfBirthCode;
    private final String cityCode;
    private final char validationCode;

    @Override
    public String toString() {
        return surnameCode +
                nameCode +
                dateOfBirthCode.toString() +
                cityCode +
                validationCode;
    }
}
