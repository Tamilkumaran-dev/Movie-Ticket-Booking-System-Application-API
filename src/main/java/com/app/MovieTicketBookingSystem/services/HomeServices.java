package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.repositories.ShowsRepo;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class HomeServices {

    private TheatreRepo theatreRepo;
    private ShowsRepo showsRepo;


    public List<Theatre> getAllTheatre(){

        List<Theatre> theatres = theatreRepo.findAll();
        System.out.println("Size" + theatres.size());
        return theatreRepo.findAll();
    }

    public List<Shows> getAllShows(){
        return showsRepo.findAll();
    }

    public Theatre selectTheatre(Long theatreId){

        Optional<Theatre> theatre = theatreRepo.findById(theatreId);
        return theatre.get();
    }

    public Shows selectShow(Long theatreId,Long showId){

        Optional<Theatre> theatre = theatreRepo.findById(theatreId);
        Optional<Shows> show = theatre.get().getShows().stream().filter(s->s.getId().equals(showId)).findFirst();
        return show.get();
    }


    public Page<Theatre> searchTheatre(String theatre, int page, int size){

        Pageable pageable = PageRequest.of(page - 1, size);

        if(theatre.equals("AllTheatre")){
            return theatreRepo.findAll(pageable);
        }

        else {
            return theatreRepo.searchTheatre(theatre,pageable);
        }
    }

    public Page<Shows> searchShows(String shows, int page, int size){

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("timing").descending());

        if(shows.equals("AllShows")){
            return showsRepo.findAllUpcomingShows(pageable);
        }
        else{
            return showsRepo.searchShows(shows,pageable);
        }
    }




}
