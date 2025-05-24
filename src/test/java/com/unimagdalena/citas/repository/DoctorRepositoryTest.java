package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.TestcontainersConfiguration;
import com.unimagdalena.citas.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoctorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findByFullName() {
        Doctor doctor = Doctor.builder()
                .fullName("Dra. María González")
                .email("maria.gonzalez@hospital.com")
                .specialty("Pediatría")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();
        entityManager.persist(doctor);

        Optional<Doctor> found = doctorRepository.findByFullName("Dra. María González");

        assertTrue(found.isPresent());
        assertEquals("maria.gonzalez@hospital.com", found.get().getEmail());
    }

    @Test
    void findBySpecialty() {
        Doctor doctor = Doctor.builder()
                .fullName("Dr. Andrés Ruiz")
                .email("andres.ruiz@hospital.com")
                .specialty("Cardiología")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();
        entityManager.persist(doctor);

        List<Doctor> cardiologos = doctorRepository.findBySpecialty("Cardiología");

        assertFalse(cardiologos.isEmpty());
        assertEquals("Cardiología", cardiologos.get(0).getSpecialty());
    }

    @Test
    void findAvailableDoctors() {
        Doctor doctor = Doctor.builder()
                .fullName("Dra. Ana Torres")
                .email("ana.torres@hospital.com")
                .specialty("Neurología")
                .availableFrom(LocalTime.of(10, 0))
                .availableTo(LocalTime.of(18, 0))
                .build();
        entityManager.persist(doctor);

        List<Doctor> disponibles = doctorRepository.findAvailableDoctors(LocalTime.of(11, 0), LocalTime.of(12, 0));

        assertFalse(disponibles.isEmpty());
        assertEquals("Dra. Ana Torres", disponibles.get(0).getFullName());
    }

    @Test
    void findAvailableDoctorsOnDate() {
        Doctor doctor = Doctor.builder()
                .fullName("Dr. Luis Mendoza")
                .email("luis.mendoza@hospital.com")
                .specialty("Dermatología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(14, 0))
                .build();
        entityManager.persist(doctor);

        List<Doctor> disponibles = doctorRepository.findAvailableDoctorsOnDate(LocalTime.of(9, 30));

        assertFalse(disponibles.isEmpty());
        assertEquals("Dr. Luis Mendoza", disponibles.get(0).getFullName());
    }
}