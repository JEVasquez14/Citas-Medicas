package com.unimagdalena.citas.mapper;

import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;
import com.unimagdalena.citas.model.ConsultRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultRoomMapper {

    ConsultRoom toModel(CreateConsultRoomDTO dto);

    ConsultRoom toModel(UpdateConsultRoomDTO dto);

    ConsultRoomResponseDTO toResponse(ConsultRoom room);
}