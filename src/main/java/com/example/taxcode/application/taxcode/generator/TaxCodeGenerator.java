package com.example.taxcode.application.taxcode.generator;

import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.factory.dto.TaxCode;

public interface TaxCodeGenerator {
    TaxCode generateTaxCode(People people);
}
