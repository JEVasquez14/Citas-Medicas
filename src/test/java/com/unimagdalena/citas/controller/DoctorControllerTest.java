package com.unimagdalena.citas.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimagdalena.citas.dto.doctor.CreateDoctorDTO;
import com.unimagdalena.citas.dto.doctor.DoctorResponseDTO;
import com.unimagdalena.citas.dto.doctor.UpdateDoctorDTO;
import com.unimagdalena.citas.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.MockConfig.class)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DoctorService doctorService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public DoctorService doctorService() {
            return Mockito.mock(DoctorService.class);
        }
    }

    @Test
    void getAllDoctors() throws Exception {
        DoctorResponseDTO doctor = new DoctorResponseDTO(1L, "Dr. Smith", "smith@example.com", "Cardiologist", LocalTime.of(8, 0), LocalTime.of(17, 0));
        when(doctorService.getAllDoctors()).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("Dr. Smith")))
                .andExpect(jsonPath("$[0].email", is("smith@example.com")));
    }

    @Test
    void getDoctorById() throws Exception {
        DoctorResponseDTO doctor = new DoctorResponseDTO(1L, "Dr. Smith", "smith@example.com", "Cardiologist", LocalTime.of(8, 0), LocalTime.of(17, 0));
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Dr. Smith")));
    }

    @Test
    void createDoctor() throws Exception {
        CreateDoctorDTO dto = new CreateDoctorDTO("Dr. Smith", "smith@example.com", "Cardiologist", LocalTime.of(8, 0), LocalTime.of(17, 0));
        DoctorResponseDTO responseDTO = new DoctorResponseDTO(1L, dto.getFullName(), dto.getEmail(), dto.getSpecialty(), dto.getAvailableFrom(), dto.getAvailableTo());

        when(doctorService.createDoctor(dto)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/doctors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Dr. Smith")));
    }

    @Test
    void updateDoctor() throws Exception {
        UpdateDoctorDTO dto = new UpdateDoctorDTO("Dr. Smith", "smith@newdomain.com", "Cardiologist", LocalTime.of(8, 0), LocalTime.of(17, 0));
        DoctorResponseDTO responseDTO = new DoctorResponseDTO(1L, dto.getFullName(), dto.getEmail(), dto.getSpecialty(), dto.getAvailableFrom(), dto.getAvailableTo());

        when(doctorService.updateDoctor(1L, dto)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("Dr. Smith")))
                .andExpect(jsonPath("$.email", is("smith@newdomain.com")));
    }

    @Test
    void deleteDoctor() throws Exception {
        doNothing().when(doctorService).deleteDoctor(1L);

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDoctors() throws Exception {
        DoctorResponseDTO doctor = new DoctorResponseDTO(1L, "Dr. Smith", "smith@example.com", "Cardiologist", LocalTime.of(8, 0), LocalTime.of(17, 0));
        when(doctorService.getDoctorsBySpecialty("Cardiologist")).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctors?specialty=Cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialty", is("Cardiologist")));
    }
}