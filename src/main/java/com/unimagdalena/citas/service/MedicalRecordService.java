package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;
import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponseDTO createMedicalRecord(CreateMedicalRecordDTO createDTO);

    MedicalRecordResponseDTO updateMedicalRecord(Long id, UpdateMedicalRecordDTO updateDTO);

    void deleteMedicalRecord(Long id);

    MedicalRecordResponseDTO getMedicalRecordById(Long id);

    List<MedicalRecordResponseDTO> getAllMedicalRecords();

    List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(Long patientId);
}
