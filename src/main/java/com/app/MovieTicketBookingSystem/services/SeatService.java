package com.app.MovieTicketBookingSystem.services;


import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.BookedSeats;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.repositories.BookedSeatsRepo;
import com.app.MovieTicketBookingSystem.repositories.ShowsRepo;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import com.app.MovieTicketBookingSystem.utils.OtpSender;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SeatService {

    private TheatreRepo theatreRepo;
    private ShowsRepo showsRepo;
    private UsersRepo usersRepo;
    private BookedSeatsRepo bookedSeatsRepo;
    private JwtUtil jwtUtil;
    private OtpSender emailSender;

    public ResponseDto bookSeats(Long TheatreId, Long ShowId, String seats, HttpServletRequest request){

        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }

        String email = jwtUtil.decodeToken(pureToken).getSubject();

        Optional<Users> user = usersRepo.findByEmail(email);
        Optional<Shows> show = showsRepo.findById(ShowId);
        Optional<Theatre> theatre = theatreRepo.findById(TheatreId);

        List<Integer> listOfSeats = Arrays.stream(seats.split(","))
                .map(
                        (s)->
                        {
                            if(show.get().getAvailableSeats().contains(Integer.parseInt(s))){
                                show.get().getAvailableSeats().remove(Integer.valueOf(Integer.parseInt(s)));
                            }
                            return Integer.parseInt(s);
                        }).toList();



        BookedSeats bookedSeats = new BookedSeats();
        bookedSeats.setShowId(ShowId);
        bookedSeats.setShowName(show.get().getShowName());
        bookedSeats.setTheatreId(TheatreId);
        bookedSeats.setTheatreName(theatre.get().getTheatreName());
        bookedSeats.setTheatreAddress(theatre.get().getAddress());
        bookedSeats.setUserId(user.get().getId());
        bookedSeats.setUserName(user.get().getName());
        bookedSeats.setShowData(show.get());
        bookedSeats.setUserData(user.get());
        bookedSeats.setSeats(listOfSeats);
        bookedSeats.setTiming(show.get().getTiming());

        bookedSeatsRepo.save(bookedSeats);

        emailSender.ticketConformationMethod(user.get().getEmail(),
                theatre.get().getTheatreName(),
                theatre.get().getAddress(),
                show.get().getShowName(),
                show.get().getTiming(),
                seats);


        return new ResponseDto("Ticket Booked successfully","Booked");
    }

    public ResponseDto bookedSeats(HttpServletRequest request){

        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }

        String email = jwtUtil.decodeToken(pureToken).getSubject();

        Optional<Users> user = usersRepo.findByEmail(email);

        List<BookedSeats> listOfBookedSeats =  user.get().getBookedTickets();

        listOfBookedSeats.sort(Comparator.comparing(BookedSeats::getTiming));


        return new ResponseDto("Booked tickets","BookedTickets", Optional.of(listOfBookedSeats));

    }



    public ResponseDto cancelSeats(Long seatId, String Seats) {
        Optional<BookedSeats> bookedSeatsOpt = bookedSeatsRepo.findById(seatId);
        if (bookedSeatsOpt.isEmpty()) {
            return new ResponseDto("No booking found with this ID", "failed");
        }

        BookedSeats bookedSeat = bookedSeatsOpt.get();
        Shows show = showsRepo.findById(bookedSeat.getShowId()).orElseThrow();
        Users user = usersRepo.findById(bookedSeat.getUserId()).orElseThrow();

        Set<Integer> cancelSeats = Arrays.stream(Seats.split(","))
                .map(Integer::valueOf)
                .collect(Collectors.toSet());

        // Remove cancelled seats
        bookedSeat.getSeats().removeAll(cancelSeats);
        show.getAvailableSeats().addAll(cancelSeats);

        // Save updated available seats
        showsRepo.save(show);

        // If no seats left, delete the booking
        if (bookedSeat.getSeats().isEmpty()) {
            show.getBookedSeats().remove(bookedSeat);
            user.getBookedTickets().remove(bookedSeat);
            bookedSeatsRepo.delete(bookedSeat);
        } else {
            bookedSeatsRepo.save(bookedSeat);
        }

        emailSender.cancelTicketMethod(
                user.getEmail(),
                show.getShowName(),
                show.getTiming(),
                Seats
        );

        return new ResponseDto("Successfully cancelled the selected tickets", "cancelled");
    }



}
