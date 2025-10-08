package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.exception.exceptions.DataAlreadyExistException;
import com.app.MovieTicketBookingSystem.exception.exceptions.InputFieldIsEmpty;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import com.app.MovieTicketBookingSystem.utils.OtpSender;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class TheatreServices {

    private TheatreRepo theatreRepo;
    private OtpSender otpSender;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public ResponseDto addNewTheatre(Theatre theatre){

        Optional<Theatre> isExist = theatreRepo.findByEmail(theatre.getEmail());

        if(isExist.isPresent()){
            throw new DataAlreadyExistException("The emailId exception");
        }

        if(theatre.getTheatreName().isEmpty() || theatre.getAddress().isEmpty() || theatre.getEmail().isEmpty() || theatre.getPassword().isEmpty()){
            throw new InputFieldIsEmpty("Some of the field are empty");
        }

        String encryptPassword = passwordEncoder.encode(theatre.getPassword());
        theatre.setPassword(encryptPassword);

        theatreRepo.save(theatre);

        return new ResponseDto("Registered","Registered");
    }

    public ResponseDto Login(LoginDto loginDto){

        Optional<Theatre> theatre = theatreRepo.findByEmail(loginDto.getEmail());
        System.out.println(theatre.get().getEmail());
        System.out.println(loginDto.getEmail());

        if(theatre.isEmpty()){
            throw  new NotFound("Email id is not exist");
        }

        else if(!passwordEncoder.matches(loginDto.getPassword(),theatre.get().getPassword())){
            throw new RuntimeException("Incorrect Password");
        }

        else if(theatre.get().isVerified()){

            String token = jwtUtil.generateJwtToken(theatre.get().getEmail());

            return new ResponseDto("successFully Logged in","Verified",token);

        }

        else{

            String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
            theatre.get().setOtp(otp);
            theatre.get().setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            theatre.get().setVerified(false);

            otpSender.sedOtpMethod(loginDto.getEmail(),otp);

            theatreRepo.save(theatre.get());

            return new ResponseDto("Verification otp send to the mailId","NotVerified");
        }

    }

    public ResponseDto emailVerify(String email, String otp){

        Optional<Theatre> theatre = theatreRepo.findByEmail(email);

        if(theatre.isEmpty()){
            throw  new NotFound("Email id is not exist");
        }


        if(theatre.get().getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("The otp is expired");
        }
        if(!(theatre.get().getOtp().equals(otp))){
            throw new RuntimeException("Invalid otp");
        }
        theatre.get().setVerified(true);
        theatre.get().setOtp(null);
        theatre.get().setOtpExpiry(null);
        theatreRepo.save(theatre.get());

        String token = jwtUtil.generateJwtToken(theatre.get().getEmail());

        return new ResponseDto("successFully Logged in","Verified",token);
    }

    public ResponseDto getUserProfile(HttpServletRequest request){

        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }

        String email = jwtUtil.decodeToken(pureToken).getSubject();

        Optional<Theatre> theatre = theatreRepo.findByEmail(email);

        return new ResponseDto("Theatre profile","Profile",theatre);
    }

    public ResponseDto addShow(HttpServletRequest request, Shows show){

        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }

        String email = jwtUtil.decodeToken(pureToken).getSubject();

        Optional<Theatre> theatre = theatreRepo.findByEmail(email);

        if (show.getShowName() == null || show.getShowName().isBlank()
                || show.getImage() == null || show.getImage().isBlank()
                || show.getTiming() == null || show.getTiming().isBefore(LocalDateTime.now())
                || show.getAvailableSeats() == null || show.getAvailableSeats().isEmpty()) {

            throw new InputFieldIsEmpty("Some of the fields are empty or invalid");
        }


        else {
            show.setTheatreId(theatre.get().getId());
            show.setTheatreName(theatre.get().getTheatreName());
            show.setTheatreAddress(theatre.get().getAddress());
            show.setTheatre(theatre.get());
            theatre.get().getShows().add(show);

            theatreRepo.save(theatre.get());

            return new ResponseDto("Show add successfully","ShowAdded");
        }

    }







}
