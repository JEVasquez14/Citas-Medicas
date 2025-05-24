package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;
import com.unimagdalena.citas.mapper.AppointmentMapper;
import com.unimagdalena.citas.model.*;
import com.unimagdalena.citas.repository.AppointmentRepository;
import com.unimagdalena.citas.repository.ConsultRoomRepository;
import com.unimagdalena.citas.repository.DoctorRepository;
import com.unimagdalena.citas.repository.PatientRepository;
import com.unimagdalena.citas.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentImplTest {

    @InjectMocks
    private AppointmentImpl appointmentImpl;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ConsultRoomRepository consultRoomRepository;


    @Test
    void createAppointment() {
        Long doctorId = 1L;
        Long patientId = 1L;
        Long consultRoomId = 1L;
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(1);

        CreateAppointmentDTO dto = new CreateAppointmentDTO();
        dto.setDoctorId(doctorId);
        dto.setPatientId(patientId);
        dto.setConsultRoomId(consultRoomId);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);

        Doctor mockDoctor = new Doctor(doctorId, "Dr. Smith", "drsmith@example.com", "123456789",
                LocalTime.of(9,0), LocalTime.of(17,0), new ArrayList<>());
        Patient mockPatient = new Patient(patientId, "John Doe", "john@example.com", "987654321",
                new ArrayList<>(), new ArrayList<>());
        ConsultRoom mockRoom = new ConsultRoom(consultRoomId, "Room 101", 1, "General", new ArrayList<>());
        Appointment savedAppointment = new Appointment(1L, mockPatient, mockDoctor, mockRoom,
                new MedicalRecord(), startTime, endTime,
                AppointmentStatus.SCHEDULED);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(mockDoctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(mockPatient));
        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(mockRoom));
        when(appointmentRepository.existsConflict(consultRoomId, startTime, endTime)).thenReturn(false);
        when(appointmentMapper.toModel(dto)).thenReturn(new Appointment());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toResponse(savedAppointment)).thenReturn(
                new AppointmentResponseDTO(1L, "John Doe", "Dr. Smith", "Room 101",
                        startTime, endTime, "SCHEDULED"));

        AppointmentResponseDTO result = appointmentImpl.createAppointment(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getPatientName());
        assertEquals("Dr. Smith", result.getDoctorName());
        assertEquals("Room 101", result.getConsultRoomName());
        assertEquals("SCHEDULED", result.getStatus());

        verify(doctorRepository).findById(doctorId);
        verify(patientRepository).findById(patientId);
        verify(consultRoomRepository).findById(consultRoomId);
        verify(appointmentRepository).existsConflict(consultRoomId, startTime, endTime);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void getAppointmentById() {
        Long appointmentId = 1L;

        Doctor doctor = new Doctor(
                1L,
                "Dr. Smith",
                "drsmith@example.com",
                "123456789",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                new ArrayList<>()
        );

        Patient patient = new Patient(
                1L,
                "John Doe",
                "john@example.com",
                "123456789",
                new ArrayList<>(),
                new ArrayList<>()
        );

        ConsultRoom consultRoom = new ConsultRoom(
                1L,
                "Room 101",
                1,
                "General Consult",
                new ArrayList<>()
        );

        MedicalRecord medicalRecord = new MedicalRecord();
        Appointment appointment = new Appointment(
                appointmentId,
                patient,
                doctor,
                consultRoom,
                medicalRecord,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                AppointmentStatus.SCHEDULED
        );

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toResponse(appointment)).thenReturn(
                new AppointmentResponseDTO(
                        appointmentId,
                        "John Doe",
                        "Dr. Smith",
                        "Room 101",
                        appointment.getStartTime(),
                        appointment.getEndTime(),
                        "PENDING"
                )
        );

        AppointmentResponseDTO response = appointmentImpl.getAppointmentById(appointmentId);

        assertNotNull(response);
        assertEquals(appointmentId, response.getId());
        assertEquals("John Doe", response.getPatientName());
        assertEquals("Dr. Smith", response.getDoctorName());
        assertEquals("Room 101", response.getConsultRoomName());
        assertEquals("PENDING", response.getStatus());
        assertEquals(appointment.getStartTime(), response.getStartTime());
        assertEquals(appointment.getEndTime(), response.getEndTime());
    }

    @Test
    void getAllAppointments() {
        Doctor doctor = new Doctor(1L, "Dr. Smith", "drsmith@example.com", "123456789",
                LocalTime.of(9,0), LocalTime.of(17,0), new ArrayList<>());

        Patient patient = new Patient(1L, "John Doe", "john@example.com", "123456789",
                new ArrayList<>(), new ArrayList<>());

        ConsultRoom room = new ConsultRoom(1L, "Room 101", 1, "General", new ArrayList<>());

        MedicalRecord record = new MedicalRecord();

        Appointment appointment1 = new Appointment();
        appointment1.setId(1L);
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        appointment1.setConsultRoom(room);
        appointment1.setMedicalRecord(record);
        appointment1.setStartTime(LocalDateTime.now());
        appointment1.setEndTime(LocalDateTime.now().plusMinutes(30));
        appointment1.setStatus(AppointmentStatus.SCHEDULED);

        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        appointment2.setPatient(patient);
        appointment2.setDoctor(doctor);
        appointment2.setConsultRoom(room);
        appointment2.setMedicalRecord(record);
        appointment2.setStartTime(LocalDateTime.now().plusHours(1));
        appointment2.setEndTime(LocalDateTime.now().plusHours(1).plusMinutes(30));
        appointment2.setStatus(AppointmentStatus.SCHEDULED);

        List<Appointment> mockAppointments = List.of(appointment1, appointment2);

        when(appointmentRepository.findAll()).thenReturn(mockAppointments);

        when(appointmentMapper.toResponse(appointment1))
                .thenReturn(new AppointmentResponseDTO(1L, "John Doe", "Dr. Smith", "Room 101",
                        appointment1.getStartTime(), appointment1.getEndTime(), "SCHEDULED"));

        when(appointmentMapper.toResponse(appointment2))
                .thenReturn(new AppointmentResponseDTO(2L, "John Doe", "Dr. Smith", "Room 101",
                        appointment2.getStartTime(), appointment2.getEndTime(), "SCHEDULED"));

        List<AppointmentResponseDTO> result = appointmentImpl.getAllAppointments();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("John Doe", result.get(0).getPatientName());
        assertEquals("Dr. Smith", result.get(0).getDoctorName());

        assertEquals(2L, result.get(1).getId());

        verify(appointmentRepository, times(1)).findAll();
        verify(appointmentMapper, times(1)).toResponse(appointment1);
        verify(appointmentMapper, times(1)).toResponse(appointment2);
    }

    @Test
    void updateAppointment() {
        Long appointmentId = 1L;

        Appointment existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setPatient(new Patient(1L, "John Doe", "john@example.com", "123456789", new ArrayList<>(), new ArrayList<>()));
        existingAppointment.setDoctor(new Doctor(1L, "Dr. Smith", "drsmith@example.com", "111111111", LocalTime.of(9,0), LocalTime.of(17,0), new ArrayList<>()));
        existingAppointment.setConsultRoom(new ConsultRoom(1L, "Room 101", 1, "General", new ArrayList<>()));
        existingAppointment.setStatus(AppointmentStatus.SCHEDULED);

        UpdateAppointmentDTO updateDTO = new UpdateAppointmentDTO();
        updateDTO.setStatus(AppointmentStatus.CANCELED);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(existingAppointment);
        when(appointmentMapper.toResponse(existingAppointment)).thenReturn(
                new AppointmentResponseDTO(
                        appointmentId,
                        "John Doe",
                        "Dr. Smith",
                        "Room 101",
                        existingAppointment.getStartTime(),
                        existingAppointment.getEndTime(),
                        "CANCELLED" // Estado actualizado
                )
        );

        AppointmentResponseDTO result = appointmentImpl.updateAppointment(appointmentId, updateDTO);

        assertNotNull(result);
        assertEquals(appointmentId, result.getId());
        assertEquals("CANCELLED", result.getStatus()); // Verificar cambio de estado
        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository).save(existingAppointment);
        verify(appointmentMapper).toResponse(existingAppointment);
    }

    @Test
    void deleteAppointment() {
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentRepository).delete(appointment);

        appointmentImpl.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).findById(appointmentId);

        verify(appointmentRepository, times(1)).delete(appointment);

        verifyNoMoreInteractions(appointmentRepository);
    }

    @Test
    void hasConflict() {
        Long consultRoomId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        when(appointmentRepository.existsConflict(consultRoomId, startTime, endTime))
                .thenReturn(true);

        boolean result = appointmentImpl.hasConflict(consultRoomId, startTime, endTime);

        assertTrue(result);
        verify(appointmentRepository, times(1))
                .existsConflict(consultRoomId, startTime, endTime);
    }

    @Test
    void getAppointmentsByDoctorAndDate() {

        Long doctorId = 1L;
        LocalDate date = LocalDate.of(2023, 6, 15);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Appointment> mockAppointments = Arrays.asList(
                new Appointment(1L, null, new Doctor(doctorId, "Dr. Smith", null, null, null, null, null),
                        null, null, LocalDateTime.of(2023, 6, 15, 10, 0),
                        LocalDateTime.of(2023, 6, 15, 11, 0), null),
                new Appointment(2L, null, new Doctor(doctorId, "Dr. Smith", null, null, null, null, null),
                        null, null, LocalDateTime.of(2023, 6, 15, 14, 0),
                        LocalDateTime.of(2023, 6, 15, 15, 0), null)
        );

        when(appointmentRepository.findByDoctorIdAndDate(doctorId, startOfDay, endOfDay))
                .thenReturn(mockAppointments);

        when(appointmentMapper.toResponse(any(Appointment.class)))
                .thenAnswer(invocation -> {
                    Appointment app = invocation.getArgument(0);
                    return new AppointmentResponseDTO(
                            app.getId(),
                            null,
                            app.getDoctor().getFullName(),
                            null,
                            app.getStartTime(),
                            app.getEndTime(),
                            null
                    );
                });

        List<AppointmentResponseDTO> result = appointmentImpl.getAppointmentsByDoctorAndDate(doctorId, date);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Dr. Smith", result.get(0).getDoctorName());
        assertEquals(LocalDateTime.of(2023, 6, 15, 10, 0), result.get(0).getStartTime());

        assertEquals(2L, result.get(1).getId());
        assertEquals(LocalDateTime.of(2023, 6, 15, 14, 0), result.get(1).getStartTime());

        verify(appointmentRepository).findByDoctorIdAndDate(doctorId, startOfDay, endOfDay);
        verify(appointmentMapper, times(2)).toResponse(any(Appointment.class));
    }
}