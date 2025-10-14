package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.HomeServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "HomePage controller",
        description = "This controller is used for Home page")
@RestController
@AllArgsConstructor
@NoArgsConstructor
public class HomeController {

    @Autowired
    private HomeServices homeServices;


    @Operation(
            summary = "Get all the theatres end point",
            description = "This end point is used for get all theatres"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get all theatres end point"
    )
    @GetMapping("/home/allTheatre")
    public ResponseEntity<List<Theatre>> allTheatre(){
        List<Theatre> theatres = homeServices.getAllTheatre();
        return new ResponseEntity<>( theatres,HttpStatus.OK);
    }


    @Operation(
            summary = "Get all the shows end point",
            description = "This end point is used for get all shows"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get all shows end point"
    )
    @GetMapping("/home/allShows")
    public ResponseEntity<List<Shows>> allShows(){
        List<Shows> shows = homeServices.getAllShows();
        return new ResponseEntity<>(shows,HttpStatus.OK);
    }

    @Operation(
            summary = "Get a specific theatre end point",
            description = "This end point is used for get a specific theatre"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get a specific theatre end point"
    )
    @GetMapping("/home/selectTheatre/{theatreId}")
    public ResponseEntity<Theatre> selectTheatre(@PathVariable Long theatreId){
        Theatre theatre = homeServices.selectTheatre(theatreId);
        return new ResponseEntity<>(theatre,HttpStatus.OK);
    }

    @Operation(
            summary = "Get a specific show end point",
            description = "This end point is used for get a specific show"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get a specific show end point"
    )
    @GetMapping("/selectShow/{theatreId}/{showId}")
    public ResponseEntity<Shows> selectShow(@PathVariable Long theatreId, @PathVariable Long showId){
        Shows show = homeServices.selectShow(theatreId,showId);
        return new ResponseEntity<>(show,HttpStatus.OK);
    }

    @Operation(
            summary = "search a specific theatre end point",
            description = "This end point is used for search a specific theatre"
    )
    @ApiResponse(
            responseCode = "200",
            description = "search a specific theatre end point"
    )
    @GetMapping("/home/search/theatre/{keyword}/{page}/{size}")
    public ResponseEntity<Page<Theatre>> searchTheatre(@PathVariable("keyword") String keyword,@PathVariable("page") int page,@PathVariable("size") int size){
        return new ResponseEntity<>(homeServices.searchTheatre(keyword,page,size),HttpStatus.OK);
    }


    @Operation(
            summary = "search a specific shows end point",
            description = "This end point is used for search a specific shows"
    )
    @ApiResponse(
            responseCode = "200",
            description = "search a specific shows end point"
    )
    @GetMapping("/home/search/shows/{keyword}/{page}/{size}")
    public ResponseEntity<Page<Shows>> searchShows(@PathVariable("keyword") String keyword,@PathVariable("page") int page,@PathVariable("size") int size){
        return new ResponseEntity<>(homeServices.searchShows(keyword,page,size),HttpStatus.OK);
    }

}
