package com.example.taxcode.application.entity.pk;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@Data
public class PersonPK implements Serializable {
    private String nameTaxCode;
    private String surnameTaxCode;
    private String dateOfBirthTaxCode;
    private String cityTaxCode;
    private char validationCharacterTaxCode;
}
