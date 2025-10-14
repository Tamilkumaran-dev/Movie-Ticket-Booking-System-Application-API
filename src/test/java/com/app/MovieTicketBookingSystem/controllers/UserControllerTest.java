package com.app.MovieTicketBookingSystem.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserServices userServices;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userServices = mock(UserServices.class);
        userController = new UserController(userServices);
    }

    @Test
    void testGetProfile_success() {
        ResponseDto mockResponse = new ResponseDto(
                "User profile fetched successfully",
                "ProfileFetched",
                Optional.empty()
        );

        when(userServices.getUserProfile(any(HttpServletRequest.class))).thenReturn(mockResponse);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ResponseEntity<ResponseDto> response = userController.getProfile(mockRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User profile fetched successfully", response.getBody().getMessage());
        verify(userServices, times(1)).getUserProfile(mockRequest);
    }

    @Test
    void testEditProfile_success() {
        ResponseDto mockResponse = new ResponseDto(
                "Profile updated successfully",
                "Updated",
                Optional.empty()
        );

        when(userServices.editProfile(any(Users.class))).thenReturn(mockResponse);

        Users user = new Users();
        ResponseEntity<ResponseDto> response = userController.editProfile(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile updated successfully", response.getBody().getMessage());
        verify(userServices, times(1)).editProfile(user);
    }
}
