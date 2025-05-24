package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.doctor.CreateDoctorDTO;
import com.unimagdalena.citas.dto.doctor.DoctorResponseDTO;
import com.unimagdalena.citas.dto.doctor.UpdateDoctorDTO;
import com.unimagdalena.citas.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    Doctor toModel(CreateDoctorDTO dto);

    Doctor toModel(UpdateDoctorDTO dto);

    DoctorResponseDTO toResponse(Doctor doctor);
}