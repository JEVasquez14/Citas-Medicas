package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.medicalRecord.CreateMedicalRecordDTO;
import com.unimagdalena.citas.dto.medicalRecord.MedicalRecordResponseDTO;
import com.unimagdalena.citas.dto.medicalRecord.UpdateMedicalRecordDTO;
import com.unimagdalena.citas.model.MedicalRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecord toModel(CreateMedicalRecordDTO dto);

    MedicalRecord toModel(UpdateMedicalRecordDTO dto);

    MedicalRecordResponseDTO toResponse(MedicalRecord medicalRecord);
}