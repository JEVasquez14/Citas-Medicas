package com.unimagdalena.citas.controller;

import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;
import com.unimagdalena.citas.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody CreatePatientDTO createDTO) {
        return ResponseEntity.ok(patientService.createPatient(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @RequestBody UpdatePatientDTO updateDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}