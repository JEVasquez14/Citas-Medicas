package com.unimagdalena.citas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimagdalena.citas.dto.consultroom.ConsultRoomResponseDTO;
import com.unimagdalena.citas.dto.consultroom.CreateConsultRoomDTO;
import com.unimagdalena.citas.dto.consultroom.UpdateConsultRoomDTO;
import com.unimagdalena.citas.service.ConsultRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultRoomController.class)
@Import(ConsultRoomControllerTest.MockConfig.class)
class ConsultRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsultRoomService consultRoomService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ConsultRoomService consultRoomService() {
            return Mockito.mock(ConsultRoomService.class);
        }
    }

    @Test
    void getAllRooms() throws Exception {
        ConsultRoomResponseDTO room = new ConsultRoomResponseDTO(1L, "Room A", 2, "General room");
        when(consultRoomService.getAllConsultRooms()).thenReturn(List.of(room));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Room A")))
                .andExpect(jsonPath("$[0].floor", is(2)))
                .andExpect(jsonPath("$[0].description", is("General room")));
    }

    @Test
    void getRoomById() throws Exception {
        ConsultRoomResponseDTO room = new ConsultRoomResponseDTO(1L, "Room A", 2, "General room");
        when(consultRoomService.getConsultRoomById(1L)).thenReturn(room);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Room A")));
    }

    @Test
    void createRoom() throws Exception {
        CreateConsultRoomDTO dto = new CreateConsultRoomDTO("Room A", 2, "General room");
        ConsultRoomResponseDTO responseDTO = new ConsultRoomResponseDTO(1L, dto.getName(), dto.getFloor(), dto.getDescription());

        when(consultRoomService.createConsultRoom(dto)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Room A")));
    }

    @Test
    void updateRoom() throws Exception {
        UpdateConsultRoomDTO dto = new UpdateConsultRoomDTO("Room A Updated", 3, "Updated description");
        ConsultRoomResponseDTO responseDTO = new ConsultRoomResponseDTO(1L, dto.getName(), dto.getFloor(), dto.getDescription());

        when(consultRoomService.updateConsultRoom(eq(1L), eq(dto))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Room A Updated")))
                .andExpect(jsonPath("$.floor", is(3)));
    }

    @Test
    void deleteRoom() throws Exception {
        doNothing().when(consultRoomService).deleteConsultRoom(1L);

        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isNoContent());
    }
}