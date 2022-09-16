package com.example.taxcode.application.service.impl;

import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.service.DecodeTaxCodeService;
import com.example.taxcode.application.taxcode.decode.TaxCodeDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecodeTaxCodeServiceImpl implements DecodeTaxCodeService {
    private final TaxCodeDecoder taxCodeDecoder;

    @Override
    public TaxCodeDecode decodeTaxCode(String taxCode){
        return taxCodeDecoder.decodeTaxCode(taxCode);
    }
}
