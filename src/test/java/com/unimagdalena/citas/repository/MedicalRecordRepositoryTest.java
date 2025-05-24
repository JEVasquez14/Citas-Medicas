package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.TestcontainersConfiguration;
import com.unimagdalena.citas.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MedicalRecordRepositoryTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    private Appointment appointment;
    private Patient patient;
    private MedicalRecord record;


    @BeforeEach
    void setUp() {
        patient = patientRepository.save(Patient.builder()
                .fullName("Lucía Gómez")
                .email("lucia@test.com")
                .phone("3001234567")
                .build());

        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Dr. Andrés Torres")
                .email("andres@medico.com")
                .specialty("Cardiología")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(17, 0))
                .build());

        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder()
                .name("Consultorio 1")
                .floor(2)
                .description("Consultorio general en el segundo piso")
                .build());

        appointment = appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(room)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build());

        record = medicalRecordRepository.save(MedicalRecord.builder()
                .appointment(appointment)
                .patient(patient)
                .diagnosis("Gripe común")
                .notes("Reposo por 3 días")
                .createdAt(LocalDateTime.now())
                .build());
    }


    @Test
    void findByAppointmentId() {
        Optional<MedicalRecord> found = medicalRecordRepository.findByAppointmentId(appointment.getId());
        assertTrue(found.isPresent());
        assertEquals(record.getId(), found.get().getId());
    }

    @Test
    void findByPatientId() {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patient.getId());
        assertFalse(records.isEmpty());
        assertEquals(1, records.size());
        assertEquals("Gripe común", records.get(0).getDiagnosis());
    }

    @Test
    void findByDiagnosis() {
        List<MedicalRecord> records = medicalRecordRepository.findByDiagnosis("Gripe común");
        assertFalse(records.isEmpty());
        assertEquals(1, records.size());
        assertEquals(patient.getId(), records.get(0).getPatient().getId());
    }

    @Test
    void findByAppointmentAndPatient() {
        Optional<MedicalRecord> found = medicalRecordRepository.findByAppointmentAndPatient(appointment.getId(), patient.getId());
        assertTrue(found.isPresent());
        assertEquals("Reposo por 3 días", found.get().getNotes());
    }
}