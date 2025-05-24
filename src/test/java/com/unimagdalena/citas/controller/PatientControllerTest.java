package com.unimagdalena.citas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;
import com.unimagdalena.citas.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.MockConfig.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientService patientService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PatientService patientService() {
            return Mockito.mock(PatientService.class);
        }
    }


    @Test
    void getAllPatients() throws Exception {
        PatientResponseDTO patient1 = new PatientResponseDTO(1L, "John Doe", "johndoe@example.com", "123456789");
        PatientResponseDTO patient2 = new PatientResponseDTO(2L, "Jane Smith", "janesmith@example.com", "987654321");

        Mockito.when(patientService.getAllPatients()).thenReturn(Collections.singletonList(patient1));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName", is(patient1.getFullName())))
                .andExpect(jsonPath("$[0].email", is(patient1.getEmail())))
                .andExpect(jsonPath("$[0].phone", is(patient1.getPhone())));
    }

    @Test
    void getPatientById() throws Exception {
        PatientResponseDTO patient = new PatientResponseDTO(1L, "John Doe", "johndoe@example.com", "123456789");

        Mockito.when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(patient.getFullName())))
                .andExpect(jsonPath("$.email", is(patient.getEmail())))
                .andExpect(jsonPath("$.phone", is(patient.getPhone())));
    }

    @Test
    void createPatient() throws Exception {
        CreatePatientDTO createDTO = new CreatePatientDTO("John Doe", "johndoe@example.com", "123456789");
        PatientResponseDTO createdPatient = new PatientResponseDTO(1L, "John Doe", "johndoe@example.com", "123456789");

        Mockito.when(patientService.createPatient(createDTO)).thenReturn(createdPatient);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(createdPatient.getFullName())))
                .andExpect(jsonPath("$.email", is(createdPatient.getEmail())))
                .andExpect(jsonPath("$.phone", is(createdPatient.getPhone())));
    }

    @Test
    void updatePatient() throws Exception {
        UpdatePatientDTO updateDTO = new UpdatePatientDTO("John Doe Updated", "johnupdated@example.com", "123456789");
        PatientResponseDTO updatedPatient = new PatientResponseDTO(1L, "John Doe Updated", "johnupdated@example.com", "123456789");

        Mockito.when(patientService.updatePatient(1L, updateDTO)).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(updatedPatient.getFullName())))
                .andExpect(jsonPath("$.email", is(updatedPatient.getEmail())))
                .andExpect(jsonPath("$.phone", is(updatedPatient.getPhone())));
    }

    @Test
    void deletePatient() throws Exception {
        Mockito.doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }
}