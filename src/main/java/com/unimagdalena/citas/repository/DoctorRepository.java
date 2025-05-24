package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByFullName(String fullName);

    List<Doctor> findBySpecialty(String specialty);

    @Query("SELECT d FROM Doctor d WHERE d.availableFrom <= :startTime AND d.availableTo >= :endTime")
    List<Doctor> findAvailableDoctors(@Param("startTime") LocalTime startTime,
                                      @Param("endTime") LocalTime endTime);

    @Query("SELECT d FROM Doctor d WHERE d.availableFrom <= :date AND d.availableTo >= :date")
    List<Doctor> findAvailableDoctorsOnDate(@Param("date") LocalTime date);

}