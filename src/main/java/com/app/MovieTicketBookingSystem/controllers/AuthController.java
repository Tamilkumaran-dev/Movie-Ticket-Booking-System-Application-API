package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import com.app.MovieTicketBookingSystem.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



@Tag(name = "authentication controller",
        description = "This controller is used for Register and login.")
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private UserServices userServices;
    private TheatreServices theatreServices;


    @Operation(
            summary = "User Register end point",
            description = "This end point is used to register a new user"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Register new user"
    )
    @PostMapping("/user/register")
    public ResponseEntity<ResponseDto> register(@RequestBody Users user){
        return new ResponseEntity<>(userServices.register(user), HttpStatus.CREATED);
    }


    @Operation(
            summary = "User login end point",
            description = "This end point is used for user login"
    )
    @ApiResponse(
            responseCode = "200",
            description = "login User"
    )
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
                    .secure(true)            // required for HTTPS
                    .path("/")               // root path
                    .maxAge(60*60*24*30)    // 12 hours
                    .sameSite("none")        // allow cross-origin
                    .build();


            response.addHeader("Set-Cookie", cookie.toString());

            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }

    @Operation(
            summary = "used for user otp validation end point",
            description = "This end point is used for otp validation"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Otp validation"
    )
    @PostMapping("/user/otpVerify/{email}/{otp}")
    public ResponseEntity<ResponseDto> otpVerify(@PathVariable("email") String email, @PathVariable("otp") String otp, HttpServletResponse response){

        ResponseDto res = userServices.emailVerify(email,otp);

        ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                .httpOnly(true)          // inaccessible to JS
                .secure(true)            // required for HTTPS
                .path("/")               // root path
                .maxAge(60*60*24*30)    // 12 hours
                .sameSite("none")        // allow cross-origin
                .build();


        response.addHeader("Set-Cookie", cookie.toString());

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }




    @Operation(
            summary = "Register new Theatre end point",
            description = "This end point is used to register new theatre"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Register new theatre"
    )
    @PostMapping("/theatre/register")
    public ResponseEntity<ResponseDto> registerTheatre(@RequestBody Theatre theatre){
        return new ResponseEntity<>(theatreServices.addNewTheatre(theatre), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Theatre login end point",
            description = "This end point is used for theatre login end point"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Theatre Login"
    )
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
                    .secure(true)            // required for HTTPS
                    .path("/")               // root path
                    .maxAge(60 *60 * 24*30)    // 12 hours
                    .sameSite("none")        // allow cross-origin
                    .build();


            response.addHeader("Set-Cookie", cookie.toString());

            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }


    @Operation(
            summary = "Theatre otp validation end point",
            description = "This end point is used for theatre otp validation"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Theatre otp validation end point"
    )
    @PostMapping("/theatre/otpVerify/{email}/{otp}")
    public ResponseEntity<ResponseDto> otpVerifyTheatre(@PathVariable("email") String email, @PathVariable("otp") String otp, HttpServletResponse response){

        ResponseDto res = theatreServices.emailVerify(email,otp);

        ResponseCookie cookie = ResponseCookie.from("jwt", res.getToken())
                .httpOnly(true)          // inaccessible to JS
                .secure(true)            // required for HTTPS
                .path("/")               // root path
                .maxAge(60*60*24*30)    // 12 hours
                .sameSite("none")        // allow cross-origin
                .build();


        response.addHeader("Set-Cookie", cookie.toString());


        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }


    @Operation(
            summary = "Logout the user end point",
            description = "This end point is used for Logout the user "
    )
    @ApiResponse(
            responseCode = "200",
            description = "User logout end point"
    )
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
