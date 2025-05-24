package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.patient.CreatePatientDTO;
import com.unimagdalena.citas.dto.patient.PatientResponseDTO;
import com.unimagdalena.citas.dto.patient.UpdatePatientDTO;
import com.unimagdalena.citas.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toModel(CreatePatientDTO dto);

    Patient toModel(UpdatePatientDTO dto);

    PatientResponseDTO toResponse(Patient patient);
}