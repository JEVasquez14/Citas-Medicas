package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.PatientMapper;
import com.unimagdalena.citas.model.Patient;
import com.unimagdalena.citas.repository.PatientRepository;
import com.unimagdalena.citas.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientResponseDTO createPatient(CreatePatientDTO createDTO) {
        if (patientRepository.findByEmail(createDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        if (patientRepository.findByPhone(createDTO.getPhone()).isPresent()) {
            throw new IllegalStateException("Phone number already in use");
        }

        Patient patient = patientMapper.toModel(createDTO);
        return patientMapper.toResponse(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, UpdatePatientDTO updateDTO) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patientRepository.findByEmail(updateDTO.getEmail())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new IllegalStateException("Email already in use");
                });

        patientRepository.findByPhone(updateDTO.getPhone())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new IllegalStateException("Phone number already in use");
                });

        existing.setFullName(updateDTO.getFullName());
        existing.setEmail(updateDTO.getEmail());
        existing.setPhone(updateDTO.getPhone());

        return patientMapper.toResponse(patientRepository.save(existing));
    }

    @Override
    public void deletePatient(Long id) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patientRepository.delete(existing);
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }
}
