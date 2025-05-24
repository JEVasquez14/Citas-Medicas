package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.PatientMapper;
import com.unimagdalena.citas.model.Patient;
import com.unimagdalena.citas.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;;

@ExtendWith(MockitoExtension.class)
class PatientImplTest {


    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientImpl patientImpl;

    @Test
    void createPatient() {
        CreatePatientDTO createDTO = new CreatePatientDTO("Ana Gómez", "ana@gmail.com", "3010000000");
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFullName(createDTO.getFullName());
        patient.setEmail(createDTO.getEmail());
        patient.setPhone(createDTO.getPhone());

        PatientResponseDTO responseDTO = new PatientResponseDTO(1L, "Ana Gómez", "ana@gmail.com", "3010000000");

        when(patientRepository.findByEmail(createDTO.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.findByPhone(createDTO.getPhone())).thenReturn(Optional.empty());
        when(patientMapper.toModel(createDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponse(patient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientImpl.createPatient(createDTO);

        assertNotNull(result);
        assertEquals("Ana Gómez", result.getFullName());
    }

    @Test
    void updatePatient() {
        Long id = 1L;
        UpdatePatientDTO updateDTO = new UpdatePatientDTO("Carlos Ruiz", "carlos@gmail.com", "3001112222");
        Patient existing = new Patient();
        existing.setId(id);
        existing.setFullName("Antiguo Nombre");
        existing.setEmail("viejo@gmail.com");
        existing.setPhone("3000000000");

        when(patientRepository.findById(id)).thenReturn(Optional.of(existing));
        when(patientRepository.findByEmail(updateDTO.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.findByPhone(updateDTO.getPhone())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(existing);
        when(patientMapper.toResponse(existing)).thenReturn(
                new PatientResponseDTO(id, updateDTO.getFullName(), updateDTO.getEmail(), updateDTO.getPhone())
        );

        PatientResponseDTO result = patientImpl.updatePatient(id, updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.getFullName(), result.getFullName());

    }

    @Test
    void deletePatient() {
        Long id = 1L;
        Patient patient = new Patient();
        patient.setId(id);

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        assertDoesNotThrow(() -> patientImpl.deletePatient(id));
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void getPatientById() {
        Long id = 1L;
        Patient patient = new Patient();
        patient.setId(id);
        patient.setFullName("Laura Torres");
        patient.setEmail("laura@gmail.com");
        patient.setPhone("3112233445");

        PatientResponseDTO responseDTO = new PatientResponseDTO(id, "Laura Torres", "laura@gmail.com", "3112233445");

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientImpl.getPatientById(id);

        assertNotNull(result);
        assertEquals("Laura Torres", result.getFullName());
    }

    @Test
    void getAllPatients() {        Patient p1 = new Patient();
        p1.setId(1L);
        p1.setFullName("Paciente Uno");
        p1.setEmail("uno@gmail.com");
        p1.setPhone("3000000001");

        Patient p2 = new Patient();
        p2.setId(2L);
        p2.setFullName("Paciente Dos");
        p2.setEmail("dos@gmail.com");
        p2.setPhone("3000000002");

        when(patientRepository.findAll()).thenReturn(List.of(p1, p2));
        when(patientMapper.toResponse(p1)).thenReturn(new PatientResponseDTO(1L, p1.getFullName(), p1.getEmail(), p1.getPhone()));
        when(patientMapper.toResponse(p2)).thenReturn(new PatientResponseDTO(2L, p2.getFullName(), p2.getEmail(), p2.getPhone()));

        List<PatientResponseDTO> result = patientImpl.getAllPatients();

        assertEquals(2, result.size());
    }
}