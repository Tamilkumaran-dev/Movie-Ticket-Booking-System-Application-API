package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.services.IsLoggedInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login checker controller",
        description = "This controller is used for check whether the user is login or not.")
@RestController
@AllArgsConstructor
@RequestMapping("/check")
public class IsLoggedInController {

    private IsLoggedInService isLoggedInService;


    @Operation(
            summary = "Check user Logged in status end point",
            description = "This end point is used for check the user logged in status"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Check user Logged in status end point"
    )
    @GetMapping("/isLoggedIn")
    public ResponseDto isLoggedInChecker(HttpServletRequest request, HttpServletResponse response){
        return isLoggedInService.isLoggedInCheckingService(request,response);
    }

}
