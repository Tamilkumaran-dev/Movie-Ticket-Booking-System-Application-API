package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.TheatreServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/theatre")
public class TheatreControllers {

    private TheatreServices theatreServices;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getProfile(HttpServletRequest request){
        return new ResponseEntity<>(theatreServices.getUserProfile(request),HttpStatus.OK);
    }

    @PostMapping("/addShows")
    public ResponseEntity<ResponseDto> addShow(@RequestBody Shows show, HttpServletRequest request){
        return new ResponseEntity<>(theatreServices.addShow(request,show),HttpStatus.CREATED);
    }



}
