package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;

import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;
import com.unimagdalena.citas.mapper.MedicalRecordMapper;
import com.unimagdalena.citas.model.Appointment;
import com.unimagdalena.citas.model.AppointmentStatus;
import com.unimagdalena.citas.model.MedicalRecord;
import com.unimagdalena.citas.model.Patient;
import com.unimagdalena.citas.repository.AppointmentRepository;
import com.unimagdalena.citas.repository.MedicalRecordRepository;
import com.unimagdalena.citas.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MedicalRecordImplTest {

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private MedicalRecordImpl medicalRecordImpl;

    @Test
    void createMedicalRecord() {
        CreateMedicalRecordDTO dto = new CreateMedicalRecordDTO(1L, 1L, "Diagnosis", "Notes");
        Appointment appointment = Appointment.builder()
                .id(1L)
                .status(AppointmentStatus.COMPLETED)
                .startTime(LocalDateTime.now().plusDays(1))
                .build();
        Patient patient = Patient.builder().id(1L).build();
        MedicalRecord record = MedicalRecord.builder().id(1L).build();
        MedicalRecordResponseDTO responseDTO = new MedicalRecordResponseDTO();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicalRecordRepository.findByAppointmentId(1L)).thenReturn(Optional.empty());
        when(medicalRecordMapper.toModel(dto)).thenReturn(record);
        when(medicalRecordRepository.save(record)).thenReturn(record);
        when(medicalRecordMapper.toResponse(record)).thenReturn(responseDTO);

        MedicalRecordResponseDTO result = medicalRecordImpl.createMedicalRecord(dto);
        assertEquals(responseDTO, result);
    }

    @Test
    void updateMedicalRecord() {
        Long id = 1L;
        UpdateMedicalRecordDTO dto = new UpdateMedicalRecordDTO("Updated diagnosis", "Updated notes");
        Appointment appointment = Appointment.builder().startTime(LocalDateTime.now().plusDays(1)).build();
        MedicalRecord existing = MedicalRecord.builder().id(id).appointment(appointment).build();
        MedicalRecordResponseDTO responseDTO = new MedicalRecordResponseDTO();

        when(medicalRecordRepository.findById(id)).thenReturn(Optional.of(existing));
        when(medicalRecordRepository.save(existing)).thenReturn(existing);
        when(medicalRecordMapper.toResponse(existing)).thenReturn(responseDTO);

        MedicalRecordResponseDTO result = medicalRecordImpl.updateMedicalRecord(id, dto);
        assertEquals(responseDTO, result);
    }

    @Test
    void deleteMedicalRecord() {
        Long id = 1L;
        MedicalRecord record = MedicalRecord.builder().id(id).build();

        when(medicalRecordRepository.findById(id)).thenReturn(Optional.of(record));

        assertDoesNotThrow(() -> medicalRecordImpl.deleteMedicalRecord(id));
        verify(medicalRecordRepository).delete(record);
    }

    @Test
    void getMedicalRecordById() {
        Long id = 1L;
        MedicalRecord record = MedicalRecord.builder().id(id).build();
        MedicalRecordResponseDTO responseDTO = new MedicalRecordResponseDTO();

        when(medicalRecordRepository.findById(id)).thenReturn(Optional.of(record));
        when(medicalRecordMapper.toResponse(record)).thenReturn(responseDTO);

        MedicalRecordResponseDTO result = medicalRecordImpl.getMedicalRecordById(id);
        assertEquals(responseDTO, result);
    }

    @Test
    void getAllMedicalRecords() {
        MedicalRecord record = new MedicalRecord();
        MedicalRecordResponseDTO response = new MedicalRecordResponseDTO();

        when(medicalRecordRepository.findAll()).thenReturn(List.of(record));
        when(medicalRecordMapper.toResponse(record)).thenReturn(response);

        List<MedicalRecordResponseDTO> result = medicalRecordImpl.getAllMedicalRecords();
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void getMedicalRecordsByPatientId() {
        Long patientId = 1L;
        MedicalRecord record = new MedicalRecord();
        MedicalRecordResponseDTO response = new MedicalRecordResponseDTO();

        when(patientRepository.existsById(patientId)).thenReturn(true);
        when(medicalRecordRepository.findByPatientId(patientId)).thenReturn(List.of(record));
        when(medicalRecordMapper.toResponse(record)).thenReturn(response);

        List<MedicalRecordResponseDTO> result = medicalRecordImpl.getMedicalRecordsByPatientId(patientId);
        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }
}