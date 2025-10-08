package com.app.MovieTicketBookingSystem.controllers;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.HomeServices;
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

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeServices homeServices;

    @GetMapping("/allTheatre")
    public ResponseEntity<List<Theatre>> allTheatre(){
        List<Theatre> theatres = homeServices.getAllTheatre();
        return new ResponseEntity<>( theatres,HttpStatus.OK);
    }

    @GetMapping("/allShows")
    public ResponseEntity<List<Shows>> allShows(){
        List<Shows> shows = homeServices.getAllShows();
        return new ResponseEntity<>(shows,HttpStatus.OK);
    }

    @GetMapping("/selectTheatre/{theatreId}")
    public ResponseEntity<Theatre> selectTheatre(@PathVariable Long theatreId){
        Theatre theatre = homeServices.selectTheatre(theatreId);
        return new ResponseEntity<>(theatre,HttpStatus.OK);
    }

    @GetMapping("/selectShow/{theatreId}/{showId}")
    public ResponseEntity<Shows> selectShow(@PathVariable Long theatreId, @PathVariable Long showId){
        Shows show = homeServices.selectShow(theatreId,showId);
        return new ResponseEntity<>(show,HttpStatus.OK);
    }

    @GetMapping("/search/theatre/{keyword}/{page}/{size}")
    public ResponseEntity<Page<Theatre>> searchTheatre(@PathVariable("keyword") String keyword,@PathVariable("page") int page,@PathVariable("size") int size){
        return new ResponseEntity<>(homeServices.searchTheatre(keyword,page,size),HttpStatus.OK);
    }

    @GetMapping("/search/shows/{keyword}/{page}/{size}")
    public ResponseEntity<Page<Shows>> searchShows(@PathVariable("keyword") String keyword,@PathVariable("page") int page,@PathVariable("size") int size){
        return new ResponseEntity<>(homeServices.searchShows(keyword,page,size),HttpStatus.OK);
    }

}
