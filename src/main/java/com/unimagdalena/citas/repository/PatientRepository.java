package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findById(Long id);

    Optional<Patient> findByEmail(String email);

    Optional<Patient> findByPhone(String phone);

    List<Patient> findAll();

    @Query("SELECT p FROM Patient p WHERE p.fullName LIKE %:name%")
    List<Patient> findByNameContaining(@Param("name") String name);

}