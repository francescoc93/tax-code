package com.example.taxcode.application.factory;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;

import java.time.LocalDate;
import java.util.List;

public class TaxCodeDecodeFactory {
    private TaxCodeDecodeFactory() {

    }

    public static TaxCodeDecode makeTaxCodeDecode(List<String> names, List<String> surnames, Gender gender,
                                                  String placeOfBirth,LocalDate dateOfBirth){

        return TaxCodeDecode.builder()
                .names(names)
                .surnames(surnames)
                .gender(gender)
                .placeOfBirth(placeOfBirth)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}
