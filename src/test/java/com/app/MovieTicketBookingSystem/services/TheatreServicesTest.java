package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.exception.exceptions.DataAlreadyExistException;
import com.app.MovieTicketBookingSystem.exception.exceptions.InputFieldIsEmpty;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import com.app.MovieTicketBookingSystem.utils.OtpSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TheatreServicesTest {

    private TheatreRepo theatreRepo;
    private OtpSender otpSender;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private TheatreServices theatreServices;

    @BeforeEach
    void setUp() {
        theatreRepo = mock(TheatreRepo.class);
        otpSender = mock(OtpSender.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);

        theatreServices = new TheatreServices(theatreRepo, otpSender, passwordEncoder, null, jwtUtil);
    }

    @Test
    void addNewTheatre_success() {
        Theatre theatre = new Theatre();
        theatre.setEmail("t@t.com");
        theatre.setPassword("pass");
        theatre.setTheatreName("PVR");
        theatre.setAddress("City");

        when(theatreRepo.findByEmail("t@t.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        ResponseDto response = theatreServices.addNewTheatre(theatre);

        assertEquals("Registered", response.getResKeyword());
        assertEquals("encodedPass", theatre.getPassword());
        verify(theatreRepo, times(1)).save(theatre);
    }

    @Test
    void addNewTheatre_existingEmail_throwsException() {
        Theatre theatre = new Theatre();
        theatre.setEmail("t@t.com");

        when(theatreRepo.findByEmail("t@t.com")).thenReturn(Optional.of(new Theatre()));

        assertThrows(DataAlreadyExistException.class, () -> theatreServices.addNewTheatre(theatre));
    }

    @Test
    void addNewTheatre_emptyFields_throwsException() {
        Theatre theatre = new Theatre();
        theatre.setEmail("");
        theatre.setTheatreName("");

        assertThrows(InputFieldIsEmpty.class, () -> theatreServices.addNewTheatre(theatre));
    }

    @Test
    void login_verifiedTheatre_returnsToken() {
        Theatre theatre = new Theatre();
        theatre.setEmail("t@t.com");
        theatre.setPassword("encoded");
        theatre.setVerified(true);

        when(theatreRepo.findByEmail("t@t.com")).thenReturn(Optional.of(theatre));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(jwtUtil.generateJwtToken("t@t.com")).thenReturn("token123");

        LoginDto dto = new LoginDto("t@t.com", "pass");
        ResponseDto response = theatreServices.Login(dto);

        assertEquals("Verified", response.getResKeyword());
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_wrongPassword_throwsException() {
        Theatre theatre = new Theatre();
        theatre.setEmail("t@t.com");
        theatre.setPassword("encoded");

        when(theatreRepo.findByEmail("t@t.com")).thenReturn(Optional.of(theatre));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        LoginDto dto = new LoginDto("t@t.com", "wrong");
        assertThrows(RuntimeException.class, () -> theatreServices.Login(dto));
    }
}
