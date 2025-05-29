package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;
import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;
import com.unimagdalena.citas.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecord toModel(CreateMedicalRecordDTO dto);

    MedicalRecord toModel(UpdateMedicalRecordDTO dto);


    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "appointment.id", target = "appointmentId")
    MedicalRecordResponseDTO toResponse(MedicalRecord medicalRecord);
}