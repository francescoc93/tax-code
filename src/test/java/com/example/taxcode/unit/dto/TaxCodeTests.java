package com.example.taxcode.unit.dto;

import com.example.taxcode.application.dto.DateBirthCode;
import com.example.taxcode.application.factory.dto.TaxCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxCodeTests {
    @Test
    @DisplayName("To string test")
    void toStringTest() {
        var dateBirthCode = new DateBirthCode("90", "B", "10");
        var taxCode = TaxCode.builder()
                .surnameCode("RSS")
                .nameCode("MRA")
                .dateOfBirthCode(dateBirthCode)
                .cityCode("A562")
                .validationCode('I')
                .build();

        assertEquals("RSSMRA90B10A562I", taxCode.toString());
    }
}
