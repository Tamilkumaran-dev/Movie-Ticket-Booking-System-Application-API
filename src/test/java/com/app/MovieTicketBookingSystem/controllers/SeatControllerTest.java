package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.services.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SeatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();
    }

    @Test
    void selectSeats_returnsCreated() throws Exception {
        ResponseDto dto = new ResponseDto(
                "Ticket Booked successfully",
                "Booked",
                Optional.empty()
        );

        when(seatService.bookSeats(anyLong(), anyLong(), anyString(), any()))
                .thenReturn(dto);

        mockMvc.perform(post("/bookSeats/selectSeats/1/2/1,2,3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Ticket Booked successfully"))
                .andExpect(jsonPath("$.resKeyword").value("Booked"));

        verify(seatService, times(1)).bookSeats(anyLong(), anyLong(), anyString(), any());
    }

    @Test
    void getBookedSeats_returnsOk() throws Exception {
        ResponseDto dto = new ResponseDto(
                "Booked tickets",
                "BookedTickets",
                Optional.empty()
        );

        when(seatService.bookedSeats(any())).thenReturn(dto);

        mockMvc.perform(get("/bookSeats/bookedSeats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booked tickets"))
                .andExpect(jsonPath("$.resKeyword").value("BookedTickets"));

        verify(seatService, times(1)).bookedSeats(any());
    }

    @Test
    void cancelSeats_returnsOk() throws Exception {
        ResponseDto dto = new ResponseDto(
                "Successfully cancelled the selected tickets",
                "cancelled",
                Optional.empty()
        );

        when(seatService.cancelSeats(anyLong(), anyString())).thenReturn(dto);

        mockMvc.perform(post("/bookSeats/cancelSeats/1/1,2,3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully cancelled the selected tickets"))
                .andExpect(jsonPath("$.resKeyword").value("cancelled"));

        verify(seatService, times(1)).cancelSeats(anyLong(), anyString());
    }
}
