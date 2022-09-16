package com.example.taxcode.unit.taxCode.stream;

import com.example.taxcode.application.entity.CityEntity;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TaxCodeCityCodeGeneratorStreamData {
    private static Stream<String> calculateCityCodeThrowException() {
        return Stream.of(null,
                "",
                "    ",
                " ");
    }

    private static Stream<Arguments> calculateCityCode() {
        var cityEntity = new CityEntity("C573","CESENA");
        var cityEntity2 = new CityEntity("B052","ASCOLI PICENO");
        var cityEntity3 = new CityEntity("A562","ROMA");

        return Stream.of(Arguments.of("cesena",cityEntity),
                Arguments.of("Ascoli  Piceno",cityEntity2),
                Arguments.of("   ROMA   ",cityEntity3));
    }
}
