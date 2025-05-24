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

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    private Doctor doctor;
    private Patient patient;
    private ConsultRoom consultRoom;

    @BeforeEach
    void setup() {
        doctor = doctorRepository.save(Doctor.builder()
                .fullName("Dr. House")
                .email("house@example.com")
                .specialty("Diagnóstico")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(17, 0))
                .build());

        patient = patientRepository.save(Patient.builder()
                .fullName("Lisa Cuddy")
                .email("cuddy@example.com")
                .phone("1234567890")
                .build());

        consultRoom = consultRoomRepository.save(ConsultRoom.builder()
                .name("101")
                .floor(1)
                .description("Sala Principal")
                .build());
    }
    private Appointment createAppointment(LocalDateTime start, LocalDateTime end) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .consultRoom(consultRoom)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    @Test
    void findByDoctorIdAndStartTimeBetween() {
        LocalDateTime now = LocalDateTime.now();
        Appointment appt = createAppointment(now.plusDays(1), now.plusDays(1).plusHours(1));
        appointmentRepository.save(appt);

        List<Appointment> found = appointmentRepository.findByDoctorIdAndStartTimeBetween(
                doctor.getId(), now, now.plusDays(2));

        assertEquals(1, found.size());
        assertEquals(appt.getId(), found.get(0).getId());
    }

    @Test
    void findByPatientId() {
        Appointment appt = createAppointment(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(1));
        appointmentRepository.save(appt);

        List<Appointment> found = appointmentRepository.findByPatientId(patient.getId());

        assertEquals(1, found.size());
        assertEquals(patient.getId(), found.get(0).getPatient().getId());
    }

    @Test
    void findByStatus() {
        Appointment appt = createAppointment(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).plusHours(1));
        appt.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appt);

        List<Appointment> found = appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED);

        assertFalse(found.isEmpty());
        assertEquals(AppointmentStatus.SCHEDULED, found.get(0).getStatus());
    }

    @Test
    void findByIdAndStatus() {
        Appointment appt = createAppointment(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).plusHours(1));
        appt.setStatus(AppointmentStatus.SCHEDULED);
        appt = appointmentRepository.save(appt);

        Appointment found = appointmentRepository.findByIdAndStatus(appt.getId(), AppointmentStatus.SCHEDULED);

        assertNotNull(found);
        assertEquals(appt.getId(), found.getId());
    }

    @Test
    void findByDoctorIdAndDate() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
        LocalDateTime end = start.plusHours(1);

        Appointment appt = createAppointment(start, end);
        appointmentRepository.save(appt);

        LocalDateTime startOfDay = start.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = start.toLocalDate().atTime(23, 59);

        List<Appointment> found = appointmentRepository.findByDoctorIdAndDate(doctor.getId(), startOfDay, endOfDay);

        assertEquals(1, found.size());
        assertEquals(doctor.getId(), found.get(0).getDoctor().getId());
    }

    @Test
    void existsConflict() {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = start.plusHours(1);
        appointmentRepository.save(createAppointment(start, end));

        boolean conflict = appointmentRepository.existsConflict(
                consultRoom.getId(), start.plusMinutes(30), end.plusMinutes(30));

        assertTrue(conflict);
    }
}