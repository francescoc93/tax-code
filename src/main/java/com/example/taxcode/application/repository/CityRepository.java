package com.example.taxcode.application.repository;

import com.example.taxcode.application.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity,String> {
    Optional<CityEntity> findByCity(String city);
}
