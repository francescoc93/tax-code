package com.example.taxcode.application.service;

import com.example.taxcode.application.factory.dto.TaxCodeDecode;

public interface DecodeTaxCodeService {
    TaxCodeDecode decodeTaxCode(String taxCode);
}
