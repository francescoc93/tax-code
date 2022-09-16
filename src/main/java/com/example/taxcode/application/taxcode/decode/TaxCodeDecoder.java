package com.example.taxcode.application.taxcode.decode;

import com.example.taxcode.application.factory.dto.TaxCodeDecode;
public interface TaxCodeDecoder {

    TaxCodeDecode decodeTaxCode(String taxCode);
}
