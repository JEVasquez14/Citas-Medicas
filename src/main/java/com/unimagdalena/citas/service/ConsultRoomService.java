package com.unimagdalena.citas.service;

import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;

import java.util.List;

public interface ConsultRoomService {
    ConsultRoomResponseDTO createConsultRoom(CreateConsultRoomDTO consultRoomCreateDTO);

    ConsultRoomResponseDTO updateConsultRoom(Long id, UpdateConsultRoomDTO consultRoomUpdateDTO);

    void deleteConsultRoom(Long id);

    ConsultRoomResponseDTO getConsultRoomById(Long id);

    List<ConsultRoomResponseDTO> getAllConsultRooms();
}
