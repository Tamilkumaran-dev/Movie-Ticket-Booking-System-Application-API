package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.services.SeatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bookSeats")
public class SeatController {

    private SeatService seatService;

    @PostMapping("/selectSeats/{TheatreId}/{ShowId}/{seats}")
    public ResponseEntity<ResponseDto> selectSeats(@PathVariable Long TheatreId,@PathVariable Long ShowId,@PathVariable String seats, HttpServletRequest request){
        return new ResponseEntity<>(seatService.bookSeats(TheatreId,ShowId,seats,request), HttpStatus.CREATED);
    }

    @GetMapping("/bookedSeats")
    public ResponseEntity<ResponseDto> getBookedSeats(HttpServletRequest request){
        return new ResponseEntity<>(seatService.bookedSeats(request),HttpStatus.OK);
    }

    @PostMapping("/cancelSeats/{seatId}/{seats}")
    public ResponseEntity<ResponseDto> cancelSeats(@PathVariable Long seatId, @PathVariable String seats){
        return new ResponseEntity<>(seatService.cancelSeats(seatId,seats),HttpStatus.OK);
    }

}
