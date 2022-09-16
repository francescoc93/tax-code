package com.example.taxcode.application.service;

import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.dto.TaxCodeResponse;

public interface GenerateTaxCodeService {
    TaxCodeResponse retrieveFromDatabaseOrGenerateTaxCode(Person person);
}
