package com.example.taxcode.application.taxcode.generator;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.Gender;

import java.time.LocalDate;

public interface TaxCodeDateBirthGenerator {

    DateBirthCode calculateDateOfBirthCode(LocalDate dateOfBirth, Gender gender);
}
