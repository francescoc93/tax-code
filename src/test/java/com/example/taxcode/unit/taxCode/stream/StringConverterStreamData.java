package com.example.taxcode.unit.taxCode.stream;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class StringConverterStreamData {
    private static Stream<String> stringConversionThrowException() {
        return Stream.of(null,
                "",
                "AAA AAAAAAAA  AAAAA",
                "RssMRa93B02c573&",
                "RSSMR@93&02C573I");
    }

    private static Stream<Arguments> stringConversion() {
        var string = "AaaA00";
        var values = new int[] {1,0,1,0,1,0};
        var string2 = "BbbB11";
        var values2 = new int[] {0,1,0,1,0,1};
        var string3 = "CcDdEeFfGgHhIiJj";
        var values3 = new int[] {5,2,7,3,9,4,13,5,15,6,17,7,19,8,21,9};
        var string4 = "2233445566778899";
        var values4 = new int[] {5,2,7,3,9,4,13,5,15,6,17,7,19,8,21,9};
        var string5 = "KkLlMmNnOoPpQqRr";
        var values5 = new int[] {2,10,4,11,18,12,20,13,11,14,3,15,6,16,8,17};
        var string6 = "SsTtUuVvWwXxYyZz";
        var values6 = new int[] {12,18,14,19,16,20,10,21,22,22,25,23,24,24,23,25};

        return Stream.of(Arguments.of(string,values),
                Arguments.of(string2,values2),
                Arguments.of(string3,values3),
                Arguments.of(string4,values4),
                Arguments.of(string5,values5),
                Arguments.of(string6,values6));
    }
}
