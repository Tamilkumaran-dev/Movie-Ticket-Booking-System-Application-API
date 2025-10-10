package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import com.app.MovieTicketBookingSystem.services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private UserServices userServices;
    private TheatreServices theatreServices;

    @PostMapping("/user/register")
    public ResponseEntity<ResponseDto> register(@RequestBody Users user){
        return new ResponseEntity<>(userServices.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){

        ResponseDto res =  userServices.login(loginDto);
        if(res.getToken() == null){
            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        else {
            System.out.println("token controller" +res.getToken());

            ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                    .httpOnly(true)          // inaccessible to JS
                    .secure(false)            // required for HTTPS
                    .path("/")               // root path
                    .maxAge(60*2)    // 12 hours
                    .sameSite("Lax")        // allow cross-origin
                    .build();

            // Add cookie to response
            response.addHeader("Set-Cookie", cookie.toString());

            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }

    @PostMapping("/user/otpVerify/{email}/{otp}")
    public ResponseEntity<ResponseDto> otpVerify(@PathVariable("email") String email, @PathVariable("otp") String otp, HttpServletResponse response){

        ResponseDto res = userServices.emailVerify(email,otp);

        ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                .httpOnly(true)          // inaccessible to JS
                .secure(false)            // required for HTTPS
                .path("/")               // root path
                .maxAge(60*2)    // 12 hours
                .sameSite("Lax")        // allow cross-origin
                .build();

        // Add cookie to response
        response.addHeader("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }


    // Theatre auth controller

    @PostMapping("/theatre/register")
    public ResponseEntity<ResponseDto> registerTheatre(@RequestBody Theatre theatre){
        return new ResponseEntity<>(theatreServices.addNewTheatre(theatre), HttpStatus.CREATED);
    }

    @PostMapping("/theatre/login")
    public ResponseEntity<ResponseDto> loginTheatre(@RequestBody LoginDto loginDto,HttpServletResponse response){


        ResponseDto res =  theatreServices.Login(loginDto);
        if(res.getToken() == null){
            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        else {
            System.out.println("token controller" + res.getToken());

            ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                    .httpOnly(true)          // inaccessible to JS
                    .secure(false)            // required for HTTPS
                    .path("/")               // root path
                    .maxAge(60 * 2)    // 12 hours
                    .sameSite("Lax")        // allow cross-origin
                    .build();

            // Add cookie to response
            response.addHeader("Set-Cookie", cookie.toString());

            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }

    @PostMapping("/theatre/otpVerify/{email}/{otp}")
    public ResponseEntity<ResponseDto> otpVerifyTheatre(@PathVariable("email") String email, @PathVariable("otp") String otp, HttpServletResponse response){

        ResponseDto res = theatreServices.emailVerify(email,otp);

        ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                .httpOnly(true)          // inaccessible to JS
                .secure(false)            // required for HTTPS
                .path("/")               // root path
                .maxAge(60*2)    // 12 hours
                .sameSite("Lax")        // allow cross-origin
                .build();

        // Add cookie to response
        response.addHeader("Set-Cookie", cookie.toString());


        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletResponse response){

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)             // remove the cookie
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(new ResponseDto("Logout successfully","Logout"),HttpStatus.OK);
    }



}
