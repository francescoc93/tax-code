package com.example.taxcode.application.repository;

import com.example.taxcode.application.factory.dto.Gender;
import com.example.taxcode.application.entity.PersonEntity;
import com.example.taxcode.application.entity.pk.PersonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, PersonPK> {
    @Query("SELECT DISTINCT p.name " +
            "FROM PersonEntity p " +
            "WHERE p.nameTaxCode = ?1 AND p.gender = ?2")
    List<String> findDistinctNames(String name, Gender gender);

    @Query("SELECT DISTINCT p.surname " +
            "FROM PersonEntity p " +
            "WHERE p.surnameTaxCode = ?1")
    List<String> findDistinctSurnames(String surname);

    @Query("SELECT p " +
            "FROM PersonEntity p " +
            "WHERE p.name = ?1 AND p.surname = ?2 " +
            "AND p.gender = ?3 AND p.placeOfBirth = ?4 " +
            "AND p.dateOfBirth= ?5")
    Optional<PersonEntity> findTaxCodeByPersonInformation(String name, String surname, Gender gender,
                                                          String placeOfBirth, LocalDate dateOfBirth);
}
