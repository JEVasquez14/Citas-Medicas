package com.unimagdalena.citas.repository;

import com.unimagdalena.citas.TestcontainersConfiguration;
import com.unimagdalena.citas.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsultRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void findByName() {
        ConsultRoom room = ConsultRoom.builder()
                .name("Sala 101")
                .floor(1)
                .description("Sala de cardiología")
                .build();
        entityManager.persist(room);

        Optional<ConsultRoom> found = consultRoomRepository.findByName("Sala 101");

        assertTrue(found.isPresent());
        assertEquals(1, found.get().getFloor());
    }

    @Test
    void findByFloor() {
        ConsultRoom room = ConsultRoom.builder()
                .name("Sala 202")
                .floor(2)
                .description("Sala de dermatología")
                .build();
        entityManager.persist(room);

        List<ConsultRoom> rooms = consultRoomRepository.findByFloor(2);

        assertFalse(rooms.isEmpty());
        assertEquals("Sala 202", rooms.get(0).getName());
    }
}