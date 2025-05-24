package com.unimagdalena.citas.service.impl;

import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;
import com.unimagdalena.citas.mapper.ConsultRoomMapper;
import com.unimagdalena.citas.model.ConsultRoom;
import com.unimagdalena.citas.repository.ConsultRoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultRoomImplTest {

    @Mock
    private ConsultRoomMapper consultRoomMapper;

    @Mock
    private ConsultRoomRepository consultRoomRepository;

    @InjectMocks
    private ConsultRoomImpl consultRoomImpl;

    @Test
    void createConsultRoom() {
        CreateConsultRoomDTO dto = new CreateConsultRoomDTO("Room A", 2, "Description A");
        ConsultRoom consultRoom = new ConsultRoom(null, "Room A", 2, "Description A", new ArrayList<>());
        ConsultRoomResponseDTO responseDTO = new ConsultRoomResponseDTO(1L, "Room A", 2, "Description A");

        when(consultRoomMapper.toModel(dto)).thenReturn(consultRoom);
        when(consultRoomRepository.save(consultRoom)).thenReturn(consultRoom);
        when(consultRoomMapper.toResponse(consultRoom)).thenReturn(responseDTO);

        ConsultRoomResponseDTO result = consultRoomImpl.createConsultRoom(dto);

        assertNotNull(result);
        assertEquals("Room A", result.getName());
        assertEquals(2, result.getFloor());
        verify(consultRoomMapper).toModel(dto);
        verify(consultRoomRepository).save(consultRoom);
    }

    @Test
    void updateConsultRoom() {
        Long consultRoomId = 1L;
        UpdateConsultRoomDTO dto = new UpdateConsultRoomDTO("Room B", 3, "Description B");
        ConsultRoom existingRoom = new ConsultRoom(consultRoomId, "Room A", 2, "Description A", new ArrayList<>());
        ConsultRoom updatedRoom = new ConsultRoom(consultRoomId, "Room B", 3, "Description B", new ArrayList<>());
        ConsultRoomResponseDTO responseDTO = new ConsultRoomResponseDTO(consultRoomId, "Room B", 3, "Description B");

        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(existingRoom));
        when(consultRoomMapper.toModel(dto)).thenReturn(updatedRoom);
        when(consultRoomRepository.save(updatedRoom)).thenReturn(updatedRoom);
        when(consultRoomMapper.toResponse(updatedRoom)).thenReturn(responseDTO);

        ConsultRoomResponseDTO result = consultRoomImpl.updateConsultRoom(consultRoomId, dto);

        assertNotNull(result);
        assertEquals("Room B", result.getName());
        assertEquals(3, result.getFloor());
        verify(consultRoomRepository).findById(consultRoomId);
        verify(consultRoomMapper).toModel(dto);
        verify(consultRoomRepository).save(updatedRoom);
    }

    @Test
    void deleteConsultRoom() {
        Long consultRoomId = 1L;
        ConsultRoom consultRoom = new ConsultRoom(consultRoomId, "Room A", 2, "Description A", new ArrayList<>());
        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(consultRoom));

        consultRoomImpl.deleteConsultRoom(consultRoomId);

        verify(consultRoomRepository).delete(consultRoom);
    }

    @Test
    void getConsultRoomById() {
        Long consultRoomId = 1L;
        ConsultRoom consultRoom = new ConsultRoom(consultRoomId, "Room A", 2, "Description A", new ArrayList<>());
        ConsultRoomResponseDTO responseDTO = new ConsultRoomResponseDTO(consultRoomId, "Room A", 2, "Description A");

        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(consultRoom));
        when(consultRoomMapper.toResponse(consultRoom)).thenReturn(responseDTO);

        ConsultRoomResponseDTO result = consultRoomImpl.getConsultRoomById(consultRoomId);

        assertNotNull(result);
        assertEquals("Room A", result.getName());
        assertEquals(2, result.getFloor());
        verify(consultRoomRepository).findById(consultRoomId);
    }

    @Test
    void getAllConsultRooms() {
        List<ConsultRoom> consultRooms = List.of(
                new ConsultRoom(1L, "Room A", 2, "Description A", new ArrayList<>()),
                new ConsultRoom(2L, "Room B", 3, "Description B", new ArrayList<>())
        );
        List<ConsultRoomResponseDTO> responseDTOs = consultRooms.stream()
                .map(room -> new ConsultRoomResponseDTO(room.getId(), room.getName(), room.getFloor(), room.getDescription()))
                .collect(Collectors.toList());

        when(consultRoomRepository.findAll()).thenReturn(consultRooms);
        when(consultRoomMapper.toResponse(any(ConsultRoom.class))).thenReturn(new ConsultRoomResponseDTO(1L, "Room A", 2, "Description A"));

        // Act
        List<ConsultRoomResponseDTO> result = consultRoomImpl.getAllConsultRooms();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(consultRoomRepository).findAll();
    }
}