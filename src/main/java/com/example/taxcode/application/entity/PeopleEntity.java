package com.example.taxcode.application.entity;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.entity.pk.PeoplePK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name="PEOPLE")
@IdClass(PeoplePK.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PeopleEntity {
    @Id
    @NotBlank
    @Column(name="NAME_TAX_CODE")
    private String nameTaxCode;
    @Id
    @NotBlank
    @Column(name="SURNAME_TAX_CODE")
    private String surnameTaxCode;
    @Id
    @NotBlank
    @Column(name="DATE_BIRTH_TAX_CODE")
    private String dateOfBirthTaxCode;
    @Id
    @NotBlank
    @Column(name="CITY_TAX_CODE")
    private String cityTaxCode;
    @Id
    @NotNull
    @Column(name="VALIDATION_CHARACTER_TAX_CODE")
    private char validationCharacterTaxCode;
    @NotBlank
    @Column(name="NAME")
    private String name;
    @NotBlank
    @Column(name="SURNAME")
    private String surname;
    @NotBlank
    @Column(name="CITY_BIRTH")
    private String placeOfBirth;
    @NotNull
    @Column(name="DATE_BIRTH")
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    @Column(name="GENDER")
    private Gender gender;
}
