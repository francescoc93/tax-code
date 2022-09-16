package com.example.taxcode.unit.taxCode.stream;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TaxCodeNameSurnameGeneratorStreamData {

    private static Stream<String> invalidInputThrowException(){
        return Stream.of("",null,"  ");
    }


    private static Stream<Arguments> calculateNameCode(){
        return Stream.of(Arguments.of("FRANCESCO","FNC"),
                Arguments.of("FRANCO","FNC"),
                Arguments.of("   FRANCESCO   ","FNC"),
                Arguments.of("MARIO","MRA"),
                Arguments.of("MARIA   GIULIA","MGL"),
                Arguments.of("maria GIULIA","MGL"),
                Arguments.of("BOB","BBO"),
                Arguments.of("AL","LAX"),
                Arguments.of("L","LXX"),
                Arguments.of("A","AXX"));
    }


    private static Stream<Arguments> calculateSurnameCode(){
        return Stream.of(Arguments.of("ROSSI","RSS"),
                Arguments.of("   ROSSI   ","RSS"),
                Arguments.of("Leone","LNE"),
                Arguments.of("Leone ROSSI","LNR"),
                Arguments.of("LI","LIX"),
                Arguments.of("L","LXX"),
                Arguments.of("I","IXX"));
    }
}
