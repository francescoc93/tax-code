package com.example.taxcode.application.taxcode.generator.impl;

import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.repository.CityRepository;
import com.example.taxcode.application.taxcode.generator.TaxCodeCityCodeGenerator;
import com.example.taxcode.application.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaxCodeCityCodeGeneratorImpl implements TaxCodeCityCodeGenerator {
    private final CityRepository cityRepository;

    @Override
    public String calculateCityCode(String placeOfBirth) {
        if (!validate(placeOfBirth)) {
            throw new IllegalArgumentException("The placeOfBirth is null or empty");
        }

        var placeOfBirthNormalized = StringUtils.normalizeSpace(placeOfBirth).toUpperCase();

        return cityRepository.findByCity(placeOfBirthNormalized)
                .orElseThrow(() -> new CityCodeNotFoundException("City '" + placeOfBirth + "' not found"))
                .getCityCode();
    }

    private boolean validate(String placeOfBirth) {
        return placeOfBirth != null && !placeOfBirth.isBlank();
    }

}
