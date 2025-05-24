package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO createPatient(CreatePatientDTO createDTO);

    PatientResponseDTO updatePatient(Long id, UpdatePatientDTO updateDTO);

    void deletePatient(Long id);

    PatientResponseDTO getPatientById(Long id);

    List<PatientResponseDTO> getAllPatients();

}
