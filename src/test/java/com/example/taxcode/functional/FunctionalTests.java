package com.example.taxcode.functional;

import com.example.taxcode.application.TaxCodeApplication;
import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.dto.Person;
import com.example.taxcode.application.factory.dto.TaxCodeDecode;
import com.example.taxcode.application.dto.TaxCodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = TaxCodeApplication.class)
@AutoConfigureMockMvc
class FunctionalTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @ParameterizedTest(name = "{index} => person={0}, taxCode={1}")
    @MethodSource("com.example.taxcode.functional.stream.FunctionalStreamData#validPerson")
    @DisplayName("Calculate tax code")
    void calculateTaxCode(Person person, TaxCodeResponse taxCode){
        var response = mockMvc.perform(post("/calculateTaxCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andReturn()
                .getResponse();

        assertEquals(200,response.getStatus());
        assertEquals(objectMapper.writeValueAsString(taxCode),response.getContentAsString());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.example.taxcode.functional.stream.FunctionalStreamData#invalidPerson")
    @DisplayName("Calculate tax code with invalid payload")
    void calculateTaxCodeBadRequest(Person person){
        var response = mockMvc.perform(post("/calculateTaxCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andReturn()
                .getResponse();

        assertEquals(400,response.getStatus());
    }

    @SneakyThrows
    @Test
    @DisplayName("Calculate tax code exception due to no city not found exception")
    void calculateTaxCodeBadRequest(){
        var person = new Person("Mario","Rossi", Gender.MALE,"Firenze",
                LocalDate.of(1990, Month.JULY,12));

        var response = mockMvc.perform(post("/calculateTaxCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andReturn()
                .getResponse();

        assertEquals(500,response.getStatus());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.example.taxcode.functional.stream.FunctionalStreamData#validTaxCode")
    @DisplayName("Decode tax code")
    void decodeTaxCode(String taxCode, TaxCodeDecode taxCodeDecode){
        var response = mockMvc.perform(get("/decodeTaxCode/{taxCode}",taxCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(200,response.getStatus());
        assertEquals(objectMapper.writeValueAsString(taxCodeDecode),response.getContentAsString());
    }


    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.example.taxcode.functional.stream.FunctionalStreamData#invalidTaxCode")
    @DisplayName("Decode tax code with internal server error")
    void calculateTaxCodeInternalServerError(String taxCode){
        var response = mockMvc.perform(get("/decodeTaxCode/{taxCode}",taxCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(500,response.getStatus());
    }
}
