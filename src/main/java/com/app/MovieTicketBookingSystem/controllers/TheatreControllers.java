package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "Theatre controller",
        description = "This controller is used for theatre user end point")
@RestController
@AllArgsConstructor
@RequestMapping("/theatre")
public class TheatreControllers {

    private TheatreServices theatreServices;


    @Operation(
            summary = "Get Theatre profile end point",
            description = "This end point is used for get the theatre profile end point"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Theatre profile end point"
    )
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getProfile(HttpServletRequest request){
        return new ResponseEntity<>(theatreServices.getUserProfile(request),HttpStatus.OK);
    }

    @Operation(
            summary = "Add new shows end point",
            description = "This end point is used for adding new shows"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Ticket booking end point"
    )
    @PostMapping("/addShows")
    public ResponseEntity<ResponseDto> addShow(@RequestBody Shows show, HttpServletRequest request){
        return new ResponseEntity<>(theatreServices.addShow(request,show),HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update theatre end point",
            description = "This end point is used for update theatre profile"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Update theatre profile end point"
    )
    @PostMapping("/updateProfile")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody Theatre theatre){
        return new ResponseEntity<>(theatreServices.updateProfile(theatre),HttpStatus.OK);
    }


}
