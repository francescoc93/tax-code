package com.example.taxcode.application.dto;

import com.example.taxcode.application.factory.dto.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class People {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotNull
    private Gender gender;
    @NotBlank
    private String placeOfBirth;
    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
}
