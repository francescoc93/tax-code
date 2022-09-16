package com.example.taxcode.unit.dto;

import com.example.taxcode.application.dto.DateBirthCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DateBirthCodeTests {

    @Test
    @DisplayName("To string test")
    void toStringTest(){
        var dateBirthCode = new DateBirthCode("90","B","10");

        assertEquals("90B10",dateBirthCode.toString());
    }
}
