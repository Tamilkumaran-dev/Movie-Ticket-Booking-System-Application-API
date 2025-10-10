package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.services.IsLoggedInService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/check")
public class IsLoggedInController {

    private IsLoggedInService isLoggedInService;

    @GetMapping("/isLoggedIn")
    public ResponseDto isLoggedInChecker(HttpServletRequest request, HttpServletResponse response){
        return isLoggedInService.isLoggedInCheckingService(request,response);
    }

}
