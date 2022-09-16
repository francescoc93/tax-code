package com.example.taxcode.application.factory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class TaxCodeDecode {
    @JsonFormat
    private List<String> names;
    @JsonFormat
    private List<String> surnames;
    @JsonFormat
    private Gender gender;
    @JsonFormat
    private String placeOfBirth;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
}
