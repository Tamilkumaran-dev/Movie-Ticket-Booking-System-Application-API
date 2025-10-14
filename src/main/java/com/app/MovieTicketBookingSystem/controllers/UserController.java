package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Users;
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


@Tag(name = "User controller",
        description = "This controller is used for user end point")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserServices userServices;


    @Operation(
            summary = "Get user Profile end point",
            description = "This end point is used for get user profile"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get user profile end point"
    )
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getProfile(HttpServletRequest request){
        return new ResponseEntity<>(userServices.getUserProfile(request),HttpStatus.OK);
    }

    @Operation(
            summary = "Edit user profile end point",
            description = "This end point is used for edit profile"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Edit user profile end point"
    )
    @PostMapping("/editProfile")
    public ResponseEntity<ResponseDto> editProfile(@RequestBody Users user){
        return new ResponseEntity<>(userServices.editProfile(user),HttpStatus.OK);
    }

}
