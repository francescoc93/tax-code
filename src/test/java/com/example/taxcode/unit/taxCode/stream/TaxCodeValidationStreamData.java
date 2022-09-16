package com.example.taxcode.unit.taxCode.stream;


import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TaxCodeValidationStreamData {
    private static Stream<String> taxCodeNotMatchRegex() {
        return Stream.of(null,
                "",
                "AAAAAAAAAAAAAAAA",
                "RSSMRA93B02C573",
                "RSSMRA93B02C573&",
                "RSS MRA 93B02 C573 I",
                "12345693B02C573I",
                "12345693B02C573IAB",
                "RSSMRAAAB02C573I",
                "RSSMRA93102C573I",
                "RSSMRA93BA2C573I");
    }

    private static Stream<Arguments> generateValidationCharacterNotValidValues() {
        return Stream.of(Arguments.of(null, null, null, null),
                Arguments.of("", "", "", ""),
                Arguments.of("AAA","AAA" ,"AAAAA","AAAAA"),
                Arguments.of("RSS","MRA","93B02","C57"),
                Arguments.of("RSS","MRA","93B02","C57&"),
                Arguments.of("123","456","93B02","C573"),
                Arguments.of("RSS","MRA","AAB02","C573"),
                Arguments.of("RSS","MRA","93102","C573"),
                Arguments.of("123","456","93 B 02","C573"),
                Arguments.of("RSS","MRA","93BA2","C573"));
    }
}
