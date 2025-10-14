package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TheatreControllersTest {

    private TheatreServices theatreServices;
    private TheatreControllers theatreControllers;

    @BeforeEach
    void setUp() {
        theatreServices = mock(TheatreServices.class);
        theatreControllers = new TheatreControllers(theatreServices);
    }

    @Test
    void testGetProfile_success() {
        ResponseDto mockResponse = new ResponseDto(
                "Profile fetched successfully",
                "Profile",
                Optional.empty()
        );
        when(theatreServices.getUserProfile(any(HttpServletRequest.class))).thenReturn(mockResponse);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ResponseEntity<ResponseDto> response = theatreControllers.getProfile(mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile fetched successfully", response.getBody().getMessage());
        verify(theatreServices, times(1)).getUserProfile(mockRequest);
    }

    @Test
    void testAddShow_success() {
        ResponseDto mockResponse = new ResponseDto(
                "Show added successfully",
                "ShowAdded",
                Optional.empty()
        );
        when(theatreServices.addShow(any(HttpServletRequest.class), any(Shows.class)))
                .thenReturn(mockResponse);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        Shows show = new Shows();
        ResponseEntity<ResponseDto> response = theatreControllers.addShow(show, mockRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Show added successfully", response.getBody().getMessage());
        verify(theatreServices, times(1)).addShow(mockRequest, show);
    }

    @Test
    void testUpdateProfile_success() {
        ResponseDto mockResponse = new ResponseDto(
                "Profile updated successfully",
                "Updated",
                Optional.empty()
        );
        when(theatreServices.updateProfile(any(Theatre.class))).thenReturn(mockResponse);

        Theatre theatre = new Theatre();
        ResponseEntity<ResponseDto> response = theatreControllers.updateProfile(theatre);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile updated successfully", response.getBody().getMessage());
        verify(theatreServices, times(1)).updateProfile(theatre);
    }
}
