package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.exception.exceptions.DataAlreadyExistException;
import com.app.MovieTicketBookingSystem.exception.exceptions.InputFieldIsEmpty;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import com.app.MovieTicketBookingSystem.utils.OtpSender;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServicesTest {

    private UsersRepo usersRepo;
    private OtpSender otpSender;
    private JwtUtil jwtUtil;
    private UserServices userServices;

    @BeforeEach
    void setUp() {
        usersRepo = mock(UsersRepo.class);
        otpSender = mock(OtpSender.class);
        jwtUtil = mock(JwtUtil.class);
        userServices = new UserServices(usersRepo, otpSender, jwtUtil);
    }

    @Test
    void register_success() {
        Users user = new Users(null, "John", 1234567890L, "john@example.com", "pass", false, null, null, null, "ROLE_USER");
        when(usersRepo.findByEmail("john@example.com")).thenReturn(Optional.empty());

        ResponseDto response = userServices.register(user);

        assertEquals("Registered successfully", response.getMessage());
        verify(usersRepo, times(1)).save(user);
    }

    @Test
    void register_existingEmail_throwsException() {
        Users user = new Users();
        user.setEmail("existing@example.com");

        when(usersRepo.findByEmail("existing@example.com")).thenReturn(Optional.of(new Users()));

        assertThrows(DataAlreadyExistException.class, () -> userServices.register(user));
    }

    @Test
    void register_emptyField_throwsException() {
        Users user = new Users();
        user.setEmail("");

        assertThrows(InputFieldIsEmpty.class, () -> userServices.register(user));
    }

    @Test
    void login_verifiedUser_returnsToken() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setPassword("pass");
        user.setVerified(true);

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateJwtToken("a@b.com")).thenReturn("token123");

        LoginDto dto = new LoginDto("a@b.com", "pass");
        ResponseDto response = userServices.login(dto);

        assertEquals("Verified", response.getResKeyword());
        assertEquals("token123", response.getToken());
    }

    @Test
    void login_unverifiedUser_sendsOtp() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setPassword("pass");
        user.setVerified(false);

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        LoginDto dto = new LoginDto("a@b.com", "pass");
        ResponseDto response = userServices.login(dto);

        assertEquals("NotVerified", response.getResKeyword());
        verify(otpSender, times(1)).sedOtpMethod(eq("a@b.com"), anyString());
    }

    @Test
    void login_wrongPassword_throwsException() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setPassword("correct");

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        LoginDto dto = new LoginDto("a@b.com", "wrong");
        assertThrows(RuntimeException.class, () -> userServices.login(dto));
    }

    @Test
    void emailVerify_success() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        user.setVerified(false);

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateJwtToken("a@b.com")).thenReturn("token123");

        ResponseDto response = userServices.emailVerify("a@b.com", "123456");

        assertEquals("Verified", response.getResKeyword());
        assertEquals("token123", response.getToken());
        assertTrue(user.isVerified());
    }

    @Test
    void emailVerify_wrongOtp_throwsException() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setOtp("111111");
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userServices.emailVerify("a@b.com", "123456"));
    }

    @Test
    void emailVerify_expiredOtp_throwsException() {
        Users user = new Users();
        user.setEmail("a@b.com");
        user.setOtp("123456");
        user.setOtpExpiry(LocalDateTime.now().minusMinutes(1));

        when(usersRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userServices.emailVerify("a@b.com", "123456"));
    }
}
