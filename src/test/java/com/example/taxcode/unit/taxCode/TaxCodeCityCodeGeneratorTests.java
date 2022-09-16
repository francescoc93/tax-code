package com.example.taxcode.unit.taxCode;

import com.example.taxcode.application.entity.CityEntity;
import com.example.taxcode.application.exception.CityCodeNotFoundException;
import com.example.taxcode.application.repository.CityRepository;
import com.example.taxcode.application.taxcode.generator.impl.TaxCodeCityCodeGeneratorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCodeCityCodeGeneratorTests {
    @InjectMocks
    private TaxCodeCityCodeGeneratorImpl taxCodeCityCodeGenerator;
    @Mock
    private CityRepository cityRepository;

    @ParameterizedTest()
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeCityCodeGeneratorStreamData#calculateCityCodeThrowException")
    @DisplayName("Calculate city code throw exception due to invalid input")
    void calculateCityCodeThrowException(String cityName) {
        assertThatThrownBy(() -> {
            taxCodeCityCodeGenerator.calculateCityCode(cityName);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{index} => placeOfBirth={0}, city = {1}")
    @MethodSource("com.example.taxcode.unit.taxCode.stream.TaxCodeCityCodeGeneratorStreamData#calculateCityCode")
    @DisplayName("Calculate city code")
    void calculateCityCode(String placeOfBirth, CityEntity city) throws CityCodeNotFoundException {
        when(cityRepository.findByCity(city.getCity())).thenReturn(Optional.of(city));

        var cityCode = taxCodeCityCodeGenerator.calculateCityCode(placeOfBirth);

        verify(cityRepository, times(1)).findByCity(city.getCity());
        assertEquals(city.getCityCode(), cityCode);
    }

    @Test()
    @DisplayName("City not found")
    void cityNotFound() {
        var placeOfBirth = "ROMA";

        when(cityRepository.findByCity(placeOfBirth)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            taxCodeCityCodeGenerator.calculateCityCode(placeOfBirth);
        }).isInstanceOf(CityCodeNotFoundException.class);
    }
}
