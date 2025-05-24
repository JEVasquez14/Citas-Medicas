package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.doctor.DoctorResponseDTO;
import com.unimagdalena.citas.dto.doctor.CreateDoctorDTO;
import com.unimagdalena.citas.dto.doctor.UpdateDoctorDTO;

import java.util.List;

public interface DoctorService {

    DoctorResponseDTO createDoctor(CreateDoctorDTO doctorCreateDTO);

    DoctorResponseDTO updateDoctor(Long id, UpdateDoctorDTO doctorUpdateDTO);

    void deleteDoctor(Long id);

    DoctorResponseDTO getDoctorById(Long id);

    List<DoctorResponseDTO> getAllDoctors();

    List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty);

}
