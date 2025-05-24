package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.TestcontainersConfiguration;
import com.unimagdalena.citas.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Import(TestcontainersConfiguration.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void findById() {
        Patient patient = Patient.builder()
                .fullName("Juan Pérez")
                .email("juan@test.com")
                .phone("1234567890")
                .build();

        patientRepository.save(patient);

        Optional<Patient> foundPatient = patientRepository.findById(patient.getId());

        assertTrue(foundPatient.isPresent());
        assertEquals(patient.getId(), foundPatient.get().getId());
        assertEquals(patient.getFullName(), foundPatient.get().getFullName());
    }

    @Test
    void findByEmail() {
        Patient patient = Patient.builder()
                .fullName("Ana López")
                .email("ana@test.com")
                .phone("0987654321")
                .build();

        patientRepository.save(patient);

        Optional<Patient> foundPatient = patientRepository.findByEmail("ana@test.com");

        assertTrue(foundPatient.isPresent());
        assertEquals(patient.getEmail(), foundPatient.get().getEmail());
    }

    @Test
    void findByPhone() {
        Patient patient = Patient.builder()
                .fullName("Carlos García")
                .email("carlos@test.com")
                .phone("5555555555")
                .build();

        patientRepository.save(patient);

        Optional<Patient> foundPatient = patientRepository.findByPhone("5555555555");

        assertTrue(foundPatient.isPresent());
        assertEquals(patient.getPhone(), foundPatient.get().getPhone());
    }

    @Test
    void findAll() {
        Patient patient1 = Patient.builder()
                .fullName("Pedro Pérez")
                .email("pedro@test.com")
                .phone("1112223333")
                .build();

        Patient patient2 = Patient.builder()
                .fullName("Laura Martínez")
                .email("laura@test.com")
                .phone("4445556666")
                .build();

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        List<Patient> patients = patientRepository.findAll();

        assertFalse(patients.isEmpty());
        assertEquals(2, patients.size());
    }

    @Test
    void findByNameContaining() {
        Patient patient1 = Patient.builder()
                .fullName("Carlos García")
                .email("carlos@test.com")
                .phone("5555555555")
                .build();

        Patient patient2 = Patient.builder()
                .fullName("Ana López")
                .email("ana@test.com")
                .phone("0987654321")
                .build();

        patientRepository.save(patient1);
        patientRepository.save(patient2);

        List<Patient> patients = patientRepository.findByNameContaining("Carlos");

        assertFalse(patients.isEmpty());
        assertEquals(1, patients.size());
        assertTrue(patients.get(0).getFullName().contains("Carlos"));
    }
}