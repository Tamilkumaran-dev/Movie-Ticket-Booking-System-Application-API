package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IsLoggedInService {

    private UsersRepo usersRepo;
    private TheatreRepo theatreRepo;
    private JwtUtil jwtUtil;

    public ResponseDto isLoggedInCheckingService(HttpServletRequest request, HttpServletResponse response){
        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }
        if(!pureToken.isEmpty()){
            String email =  jwtUtil.decodeToken(pureToken).getSubject();
            Optional<Users> user = usersRepo.findByEmail(email);
            Optional<Theatre> theatre = theatreRepo.findByEmail(email);

            if(user.isPresent()){
                return new ResponseDto("User logged in",user.get().getRole());
            }
            else if(theatre.isPresent()){
                return new ResponseDto("Theatre logged in",theatre.get().getRole());
            }
            else {

                ResponseCookie cookie = ResponseCookie.from("jwt", "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(0)             // remove the cookie
                        .sameSite("None")
                        .build();

                response.addHeader("Set-Cookie", cookie.toString());

                throw new NotFound("fake token");

            }

        }
        else{
            throw new NotFound("Not logged in");
        }
    }

}
