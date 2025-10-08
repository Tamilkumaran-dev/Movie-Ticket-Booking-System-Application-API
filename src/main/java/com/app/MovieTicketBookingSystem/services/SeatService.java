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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
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
        bookedSeats.setTheatreId(TheatreId);
        bookedSeats.setUserId(user.get().getId());
        bookedSeats.setShowData(show.get());
        bookedSeats.setUserData(user.get());
        bookedSeats.setSeats(listOfSeats);
        bookedSeats.setTiming(show.get().getTiming());

        bookedSeatsRepo.save(bookedSeats);

        emailSender.ticketConformationMethod(user.get().getEmail(),
                theatre.get().getTheatreName(),
                theatre.get().getAddress(),
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

        listOfBookedSeats.sort((a, b) -> b.getTiming().compareTo(a.getTiming()));


        return new ResponseDto("Booked tickets","BookedTickets", Optional.of(listOfBookedSeats));

    }

    public ResponseDto cancelSeats(Long seatId,String Seats){

        Optional<BookedSeats> bookedSeats = bookedSeatsRepo.findById(seatId);
        Optional<Shows> shows = showsRepo.findById(bookedSeats.get().getShowId());


        Set<Integer> cancelSeats = Arrays.stream(Seats.split(",")).map(s ->
        {
            if(bookedSeats.get().getSeats().contains(Integer.valueOf(s))){
                bookedSeats.get().getSeats().remove(Integer.valueOf(s));
                shows.get().getAvailableSeats().add(Integer.parseInt(s));
            }
            return Integer.valueOf(s);

        }).collect(Collectors.toSet());



        if(bookedSeats.get().getSeats().isEmpty()){
            bookedSeatsRepo.deleteById(seatId);
        }
        else {
            bookedSeatsRepo.save(bookedSeats.get());
        }

        return new ResponseDto("Successfully tickets are cancelled","cancelled");

    }


}
