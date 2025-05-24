package com.unimagdalena.citas.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;
import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;
import com.unimagdalena.citas.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MedicalRecordController.class)
@Import(MedicalRecordControllerTest.MockConfig.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public MedicalRecordService medicalRecordService() {
            return Mockito.mock(MedicalRecordService.class);
        }
    }

    @Test
    void getAllRecords() throws Exception {
        MedicalRecordResponseDTO record1 = new MedicalRecordResponseDTO(1L, 1L, 1L, "Diagnosis 1", "Notes 1", LocalDateTime.now());
        MedicalRecordResponseDTO record2 = new MedicalRecordResponseDTO(2L, 2L, 2L, "Diagnosis 2", "Notes 2", LocalDateTime.now());
        List<MedicalRecordResponseDTO> records = List.of(record1, record2);

        Mockito.when(medicalRecordService.getAllMedicalRecords()).thenReturn(records);

        mockMvc.perform(get("/api/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(records.size()))
                .andExpect(jsonPath("$[0].diagnosis").value("Diagnosis 1"));
    }

    @Test
    void getRecordById() throws Exception {
        MedicalRecordResponseDTO record = new MedicalRecordResponseDTO(1L, 1L, 1L, "Diagnosis 1", "Notes 1", LocalDateTime.now());

        Mockito.when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(record);

        mockMvc.perform(get("/api/records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Diagnosis 1"))
                .andExpect(jsonPath("$.notes").value("Notes 1"));
    }

    @Test
    void getRecordsByPatientId() throws Exception {
        MedicalRecordResponseDTO record1 = new MedicalRecordResponseDTO(1L, 1L, 1L, "Diagnosis 1", "Notes 1", LocalDateTime.now());
        MedicalRecordResponseDTO record2 = new MedicalRecordResponseDTO(2L, 2L, 2L, "Diagnosis 2", "Notes 2", LocalDateTime.now());
        List<MedicalRecordResponseDTO> records = List.of(record1, record2);

        Mockito.when(medicalRecordService.getMedicalRecordsByPatientId(1L)).thenReturn(records);

        mockMvc.perform(get("/api/records/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(records.size()))
                .andExpect(jsonPath("$[0].diagnosis").value("Diagnosis 1"));
    }

    @Test
    void createRecord() throws Exception {
        CreateMedicalRecordDTO createDTO = new CreateMedicalRecordDTO(1L, 1L, "Diagnosis 1", "Notes 1");
        MedicalRecordResponseDTO createdRecord = new MedicalRecordResponseDTO(1L, 1L, 1L, "Diagnosis 1", "Notes 1", LocalDateTime.now());

        Mockito.when(medicalRecordService.createMedicalRecord(Mockito.any(CreateMedicalRecordDTO.class))).thenReturn(createdRecord);

        mockMvc.perform(post("/api/records")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diagnosis").value("Diagnosis 1"))
                .andExpect(jsonPath("$.notes").value("Notes 1"));
    }

    @Test
    void updateRecord() throws Exception {
        UpdateMedicalRecordDTO updateDTO = new UpdateMedicalRecordDTO("Updated Diagnosis", "Updated Notes");
        MedicalRecordResponseDTO updatedRecord = new MedicalRecordResponseDTO(1L, 1L, 1L, "Updated Diagnosis", "Updated Notes", LocalDateTime.now());

        Mockito.when(medicalRecordService.updateMedicalRecord(Mockito.anyLong(), Mockito.any(UpdateMedicalRecordDTO.class)))
                .thenReturn(updatedRecord);

        mockMvc.perform(put("/api/records/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Updated Diagnosis"))
                .andExpect(jsonPath("$.notes").value("Updated Notes"));
    }

    @Test
    void deleteRecord() throws Exception {
        Mockito.doNothing().when(medicalRecordService).deleteMedicalRecord(1L);

        mockMvc.perform(delete("/api/records/1"))
                .andExpect(status().isNoContent());
    }
}