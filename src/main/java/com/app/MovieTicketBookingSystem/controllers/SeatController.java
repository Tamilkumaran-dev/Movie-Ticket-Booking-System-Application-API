package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.services.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Seats controller",
        description = "This controller is used perform the seats API end point")
@RestController
@AllArgsConstructor
@RequestMapping("/bookSeats")
public class SeatController {

    private SeatService seatService;


    @Operation(
            summary = "Select a seats end point",
            description = "This end point is used for select seats and book seats for a show"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Ticket booking end point"
    )
    @PostMapping("/selectSeats/{TheatreId}/{ShowId}/{seats}")
    public ResponseEntity<ResponseDto> selectSeats(@PathVariable Long TheatreId,@PathVariable Long ShowId,@PathVariable String seats, HttpServletRequest request){
        return new ResponseEntity<>(seatService.bookSeats(TheatreId,ShowId,seats,request), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get the book tickets seats end point",
            description = "This end point is used for get all the booked tickets"
    )
    @ApiResponse(
            responseCode = "200",
            description = "get booked tickets end point"
    )
    @GetMapping("/bookedSeats")
    public ResponseEntity<ResponseDto> getBookedSeats(HttpServletRequest request){
        return new ResponseEntity<>(seatService.bookedSeats(request),HttpStatus.OK);
    }

    @Operation(
            summary = "Cancel the book tickets seats end point",
            description = "This end point is used for cancel booked tickets"
    )
    @ApiResponse(
            responseCode = "200",
            description = "cancel the booked tickets end point"
    )
    @PostMapping("/cancelSeats/{seatId}/{seats}")
    public ResponseEntity<ResponseDto> cancelSeats(@PathVariable Long seatId, @PathVariable String seats){
        return new ResponseEntity<>(seatService.cancelSeats(seatId,seats),HttpStatus.OK);
    }

}
