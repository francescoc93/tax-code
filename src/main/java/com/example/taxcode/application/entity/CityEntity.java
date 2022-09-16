package com.example.taxcode.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="CITIES")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CityEntity {
    @Id
    @Column(name = "CITY_CODE")
    private String cityCode;
    @Column(name="CITY")
    private String city;
}
