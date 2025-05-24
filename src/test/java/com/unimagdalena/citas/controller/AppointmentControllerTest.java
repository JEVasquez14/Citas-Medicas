package com.unimagdalena.citas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimagdalena.citas.dto.appointment.AppointmentResponseDTO;
import com.unimagdalena.citas.dto.appointment.CreateAppointmentDTO;
import com.unimagdalena.citas.dto.appointment.UpdateAppointmentDTO;
import com.unimagdalena.citas.model.Appointment;
import com.unimagdalena.citas.model.AppointmentStatus;
import com.unimagdalena.citas.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import(AppointmentControllerTest.MockConfig.class)
class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AppointmentService appointmentService() {
            return Mockito.mock(AppointmentService.class);
        }
    }


    @Test
    void getAllAppointments() throws Exception {
        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO();
        appointmentResponse.setId(1L);
        appointmentResponse.setStartTime(LocalDateTime.now());
        appointmentResponse.setEndTime(LocalDateTime.now().plusHours(1));
        appointmentResponse.setStatus("Scheduled");

        List<AppointmentResponseDTO> appointments = List.of(appointmentResponse);

        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("Scheduled"));
    }

    @Test
    void getAppointmentById() throws Exception {
        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO();
        appointmentResponse.setId(1L);
        appointmentResponse.setStartTime(LocalDateTime.now());
        appointmentResponse.setEndTime(LocalDateTime.now().plusHours(1));
        appointmentResponse.setStatus("Scheduled");

        when(appointmentService.getAppointmentById(1L)).thenReturn(appointmentResponse);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("Scheduled"));
    }

    @Test
    void createAppointment() throws Exception {
        CreateAppointmentDTO dto = new CreateAppointmentDTO();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setConsultRoomId(1L);
        dto.setStartTime(LocalDateTime.now().plusMinutes(30));
        dto.setEndTime(LocalDateTime.now().plusHours(1));

        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO();
        appointmentResponse.setId(1L);
        appointmentResponse.setStartTime(dto.getStartTime());
        appointmentResponse.setEndTime(dto.getEndTime());
        appointmentResponse.setStatus("Scheduled");

        when(appointmentService.createAppointment(any(CreateAppointmentDTO.class))).thenReturn(appointmentResponse);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("Scheduled"));
    }

    @Test
    void updateAppointment() throws Exception {
        UpdateAppointmentDTO dto = new UpdateAppointmentDTO();
        dto.setDoctorId(1L);
        dto.setPatientId(1L);
        dto.setConsultRoomId(1L);
        dto.setStartTime(LocalDateTime.now().plusMinutes(30));
        dto.setEndTime(LocalDateTime.now().plusHours(1));
        dto.setStatus(AppointmentStatus.SCHEDULED);

        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO();
        appointmentResponse.setId(1L);
        appointmentResponse.setStatus("SCHEDULED");
        appointmentResponse.setStartTime(dto.getStartTime());
        appointmentResponse.setEndTime(dto.getEndTime());
        appointmentResponse.setDoctorName("Dr. Smith");
        appointmentResponse.setPatientName("John Doe");
        appointmentResponse.setConsultRoomName("Room 101");

        when(appointmentService.updateAppointment(eq(1L), any(UpdateAppointmentDTO.class)))
                .thenReturn(appointmentResponse);

        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.doctorName").value("Dr. Smith"))
                .andExpect(jsonPath("$.patientName").value("John Doe"));
    }

    @Test
    void deleteAppointment() throws Exception {
        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAppointmentsByDoctorAndDate() throws Exception {
        AppointmentResponseDTO appointmentResponse = new AppointmentResponseDTO();
        appointmentResponse.setId(1L);
        appointmentResponse.setStartTime(LocalDateTime.now());
        appointmentResponse.setEndTime(LocalDateTime.now().plusHours(1));
        appointmentResponse.setStatus("Scheduled");

        List<AppointmentResponseDTO> appointments = List.of(appointmentResponse);

        when(appointmentService.getAppointmentsByDoctorAndDate(eq(1L), any(LocalDate.class))).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments")
                        .param("doctorId", "1")
                        .param("date", "2025-04-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("Scheduled"));
    }
}