package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;
import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.MedicalRecordMapper;
import com.unimagdalena.citas.model.Appointment;
import com.unimagdalena.citas.model.AppointmentStatus;
import com.unimagdalena.citas.model.MedicalRecord;
import com.unimagdalena.citas.model.Patient;
import com.unimagdalena.citas.repository.AppointmentRepository;
import com.unimagdalena.citas.repository.MedicalRecordRepository;
import com.unimagdalena.citas.repository.PatientRepository;
import com.unimagdalena.citas.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordImpl implements MedicalRecordService {


    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordMapper medicalRecordMapper;


    @Override
    public MedicalRecordResponseDTO createMedicalRecord(CreateMedicalRecordDTO createDTO) {

        Appointment appointment = appointmentRepository.findById(createDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Patient patient = patientRepository.findById(createDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot create medical record for an appointment not completed");
        }

        if (medicalRecordRepository.findByAppointmentId(appointment.getId()).isPresent()) {
            throw new IllegalStateException("Medical record already exists for this appointment");
        }

        MedicalRecord record = medicalRecordMapper.toModel(createDTO);
        record.setAppointment(appointment);
        record.setPatient(patient);
        record.setCreatedAt(LocalDateTime.now());

        return medicalRecordMapper.toResponse(medicalRecordRepository.save(record));
    }

    @Override
    public MedicalRecordResponseDTO updateMedicalRecord(Long id, UpdateMedicalRecordDTO updateDTO) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        if (existing.getAppointment().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot update medical record for a past appointment");
        }

        existing.setDiagnosis(updateDTO.getDiagnosis());
        existing.setNotes(updateDTO.getNotes());

        return medicalRecordMapper.toResponse(medicalRecordRepository.save(existing));
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        medicalRecordRepository.delete(record);
    }

    @Override
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        return medicalRecordMapper.toResponse(record);
    }

    @Override
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(medicalRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found");
        }

        return medicalRecordRepository.findByPatientId(patientId)
                .stream()
                .map(medicalRecordMapper::toResponse)
                .collect(Collectors.toList());
    }
}
