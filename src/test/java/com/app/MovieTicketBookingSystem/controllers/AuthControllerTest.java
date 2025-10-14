package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import com.app.MovieTicketBookingSystem.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.Cookie;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServices userServices;

    @Mock
    private TheatreServices theatreServices;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void registerUser_returnsCreated() throws Exception {
        Users u = new Users();
        u.setEmail("a@b.com");
        u.setName("name");
        u.setPassword("pass");
        ResponseDto dto = new ResponseDto("Registered successfully","Registered");

        when(userServices.register(any(Users.class))).thenReturn(dto);

        String json = "{\"email\":\"a@b.com\",\"name\":\"name\",\"password\":\"pass\",\"mobileNo\":123456789}";

        mockMvc.perform(post("/auth/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resKeyword").value("Registered"));

        verify(userServices, times(1)).register(any(Users.class));
    }

    @Test
    void loginUser_withToken_setsCookieAndReturnsOk() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("a@b.com");
        loginDto.setPassword("pass");

        ResponseDto dto = new ResponseDto("Successfully Logged in","Verified","dummy-token");

        when(userServices.login(any(LoginDto.class))).thenReturn(dto);

        String json = "{\"email\":\"a@b.com\",\"password\":\"pass\"}";

        mockMvc.perform(post("/auth/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("jwt=dummy-token")))
                .andExpect(jsonPath("$.resKeyword").value("Verified"));

        verify(userServices, times(1)).login(any(LoginDto.class));
    }

    @Test
    void loginUser_withoutToken_returnsOk_noCookie() throws Exception {
        ResponseDto dto = new ResponseDto("Not Verified","NotVerified", (String) null);

        when(userServices.login(any(LoginDto.class))).thenReturn(dto);

        String json = "{\"email\":\"a@b.com\",\"password\":\"pass\"}";

        mockMvc.perform(post("/auth/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resKeyword").value("NotVerified"));

        verify(userServices, times(1)).login(any(LoginDto.class));
    }

    @Test
    void otpVerify_user_setsCookie_and_returnsAccepted() throws Exception {
        ResponseDto dto = new ResponseDto("Successfully Logged in","Verified","token-otp");
        when(userServices.emailVerify(eq("a@b.com"), eq("123456"))).thenReturn(dto);

        mockMvc.perform(post("/auth/user/otpVerify/a@b.com/123456"))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Set-Cookie", containsString("jwt=token-otp")))
                .andExpect(jsonPath("$.resKeyword").value("Verified"));

        verify(userServices, times(1)).emailVerify("a@b.com","123456");
    }

    @Test
    void registerTheatre_returnsCreated() throws Exception {
        Theatre t = new Theatre();
        t.setEmail("theatre@b.com");
        t.setTheatreName("MyTheatre");
        ResponseDto dto = new ResponseDto("Register successfully","Registered");

        when(theatreServices.addNewTheatre(any(Theatre.class))).thenReturn(dto);

        String json = "{\"email\":\"theatre@b.com\",\"theatreName\":\"MyTheatre\",\"password\":\"pw\",\"address\":\"addr\"}";

        mockMvc.perform(post("/auth/theatre/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resKeyword").value("Registered"));

        verify(theatreServices, times(1)).addNewTheatre(any(Theatre.class));
    }

    @Test
    void loginTheatre_setsCookie_whenTokenPresent() throws Exception {
        ResponseDto dto = new ResponseDto("successFully Logged in","Verified","theatre-token");
        when(theatreServices.Login(any(LoginDto.class))).thenReturn(dto);

        String json = "{\"email\":\"theatre@b.com\",\"password\":\"pw\"}";

        mockMvc.perform(post("/auth/theatre/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("jwt=theatre-token")))
                .andExpect(jsonPath("$.resKeyword").value("Verified"));

        verify(theatreServices, times(1)).Login(any(LoginDto.class));
    }

    @Test
    void otpVerifyTheatre_setsCookie_and_returnsAccepted() throws Exception {
        ResponseDto dto = new ResponseDto("successFully Logged in","Verified","ttoken");
        when(theatreServices.emailVerify("t@b.com","654321")).thenReturn(dto);

        mockMvc.perform(post("/auth/theatre/otpVerify/t@b.com/654321"))
                .andExpect(status().isAccepted())
                .andExpect(header().string("Set-Cookie", containsString("jwt=ttoken")))
                .andExpect(jsonPath("$.resKeyword").value("Verified"));

        verify(theatreServices, times(1)).emailVerify("t@b.com","654321");
    }

    @Test
    void logout_clearsCookie_and_returnsOk() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("jwt=")))
                .andExpect(jsonPath("$.message").value("Logout successfully"));
    }
}
