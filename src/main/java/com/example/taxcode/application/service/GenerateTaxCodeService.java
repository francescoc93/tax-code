package com.example.taxcode.application.service;

import com.example.taxcode.application.dto.People;
import com.example.taxcode.application.dto.TaxCodeResponse;

public interface GenerateTaxCodeService {
    TaxCodeResponse retrieveFromDatabaseOrGenerateTaxCode(People people);
}
