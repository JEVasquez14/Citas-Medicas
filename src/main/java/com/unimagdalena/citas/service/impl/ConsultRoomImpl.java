package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;
import com.unimagdalena.citas.exception.ConflictException;
import com.unimagdalena.citas.exception.ResourceNotFoundException;
import com.unimagdalena.citas.mapper.ConsultRoomMapper;
import com.unimagdalena.citas.model.ConsultRoom;
import com.unimagdalena.citas.repository.ConsultRoomRepository;
import com.unimagdalena.citas.service.ConsultRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultRoomImpl implements ConsultRoomService {

    private final ConsultRoomRepository consultRoomRepository;
    private final ConsultRoomMapper consultRoomMapper;

    @Override
    public ConsultRoomResponseDTO createConsultRoom(CreateConsultRoomDTO consultRoomCreateDTO) {
        if (consultRoomRepository.findByName(consultRoomCreateDTO.getName()).isPresent()) {
            throw new ConflictException("Ya existe un consultorio con el nombre: " + consultRoomCreateDTO.getName());
        }

        ConsultRoom room = consultRoomMapper.toModel(consultRoomCreateDTO);
        return consultRoomMapper.toResponse(consultRoomRepository.save(room));
    }



    @Override
    public ConsultRoomResponseDTO updateConsultRoom(Long id, UpdateConsultRoomDTO consultRoomUpdateDTO) {
        ConsultRoom existingRoom = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));

        ConsultRoom updatedRoom = consultRoomMapper.toModel(consultRoomUpdateDTO);

        updatedRoom.setId(existingRoom.getId());

        return consultRoomMapper.toResponse(consultRoomRepository.save(updatedRoom));
    }

    @Override
    public void deleteConsultRoom(Long id) {
        ConsultRoom room = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));
        consultRoomRepository.delete(room);
    }

    @Override
    public ConsultRoomResponseDTO getConsultRoomById(Long id) {
        ConsultRoom room = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado con ID: " + id));
        return consultRoomMapper.toResponse(room);
    }

    @Override
    public List<ConsultRoomResponseDTO> getAllConsultRooms() {
        return consultRoomRepository.findAll().stream()
                .map(consultRoomMapper::toResponse)
                .toList();
    }
}
