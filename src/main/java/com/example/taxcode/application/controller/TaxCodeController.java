package com.example.taxcode.application.controller;

import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import com.example.taxcode.application.service.DecodeTaxCodeService;
import com.example.taxcode.application.service.GenerateTaxCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaxCodeController {

    private final GenerateTaxCodeService generateTaxCodeService;
    private final DecodeTaxCodeService decodeTaxCodeService;

    @PostMapping("/calculateTaxCode")
    public TaxCodeResponse calculateTaxCode(@RequestBody @Valid Person person) {
        return generateTaxCodeService.retrieveFromDatabaseOrGenerateTaxCode(person);
    }

    @GetMapping("/decodeTaxCode/{taxCode}")
    public TaxCodeDecode decodeTaxCode(@PathVariable String taxCode) {
        return decodeTaxCodeService.decodeTaxCode(taxCode);
    }
}
