package com.example.taxcode.application.taxcode.generator;

import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.dto.TaxCode;

public interface TaxCodeGenerator {
    TaxCode generateTaxCode(Person person);
}
