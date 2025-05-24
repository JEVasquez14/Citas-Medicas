package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.doctor.CreateDoctorDTO;
import com.unimagdalena.citas.dto.doctor.DoctorResponseDTO;
import com.unimagdalena.citas.dto.doctor.UpdateDoctorDTO;
import com.unimagdalena.citas.exception.ConflictException;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.DoctorMapper;
import com.unimagdalena.citas.model.Doctor;
import com.unimagdalena.citas.repository.DoctorRepository;
import com.unimagdalena.citas.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorImpl implements DoctorService {

    private DoctorRepository doctorRepository;
    private DoctorMapper doctorMapper;

    @Override
    public DoctorResponseDTO createDoctor(CreateDoctorDTO doctorCreateDTO) {
        Doctor doctor = doctorMapper.toModel(doctorCreateDTO);

        Optional<Doctor> existingDoctor = doctorRepository.findByFullName(doctorCreateDTO.getFullName());
        if (existingDoctor.isPresent()) {
            throw new ConflictException("Ya existe un doctor con este nombre");
        }

        Doctor savedDoctor = doctorRepository.save(doctor);

        return doctorMapper.toResponse(savedDoctor);
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, UpdateDoctorDTO doctorUpdateDTO) {

        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Doctor no encontrado");
        }

        Doctor existingDoctor = existingDoctorOpt.get();

        existingDoctor.setFullName(doctorUpdateDTO.getFullName());
        existingDoctor.setEmail(doctorUpdateDTO.getEmail());
        existingDoctor.setSpecialty(doctorUpdateDTO.getSpecialty());
        existingDoctor.setAvailableFrom(doctorUpdateDTO.getAvailableFrom());
        existingDoctor.setAvailableTo(doctorUpdateDTO.getAvailableTo());

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);

        return doctorMapper.toResponse(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {

        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Doctor no encontrado");
        }

        doctorRepository.deleteById(id);
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {

        Optional<Doctor> existingDoctorOpt = doctorRepository.findById(id);
        if (existingDoctorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Doctor no encontrado");
        }

        return doctorMapper.toResponse(existingDoctorOpt.get());
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDTO> getDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
        return doctors.stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }
}
