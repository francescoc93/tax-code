package com.example.taxcode.application.taxcode.decode;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;

import java.time.LocalDate;

public interface TaxCodeDateBirthDecoder {
    LocalDate decodeDateOfBirth(DateBirthCode dateBirthCode);

    Gender decodeGender(DateBirthCode dateBirthCode);
}
