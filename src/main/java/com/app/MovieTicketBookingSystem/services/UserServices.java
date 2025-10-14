package com.app.MovieTicketBookingSystem.services;


import com.app.MovieTicketBookingSystem.dto.LoginDto;
import com.app.MovieTicketBookingSystem.dto.ResponseDto;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.exception.exceptions.DataAlreadyExistException;
import com.app.MovieTicketBookingSystem.exception.exceptions.InputFieldIsEmpty;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import com.app.MovieTicketBookingSystem.utils.OtpSender;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserServices {

    private UsersRepo usersRepo;
    private OtpSender otpSender;
    private JwtUtil jwtUtil;

    public ResponseDto register(Users user){
        Optional<Users> isExist = usersRepo.findByEmail(user.getEmail());

        if(isExist.isPresent()){
            throw new DataAlreadyExistException("The emailId is already used");
        }
        if(user.getEmail().isEmpty() || user.getName().isEmpty() || user.getPassword().isEmpty() || user.getMobileNo() == null){
            throw new InputFieldIsEmpty("Some of the fields are not filled");
        }

        usersRepo.save(user);
        return new ResponseDto("Registered successfully","Registered");
    }

    public ResponseDto login(LoginDto login){

        Optional<Users> isExist = usersRepo.findByEmail(login.getEmail());

        if(isExist.isEmpty()){
            throw new NotFound("This email doesn't exist");
        }
        else if(!login.getPassword().equals(isExist.get().getPassword())){
            throw new RuntimeException("Password is incorrect");
        }
        else if(isExist.get().isVerified()){

            System.out.println("Generate token");

            String tokrn = jwtUtil.generateJwtToken(isExist.get().getEmail());

            System.out.println("Token" + tokrn);
            return new ResponseDto("Successfully Logged in","Verified", tokrn);
        }
        else{

            System.out.println("Valid user");

            String otp = String.valueOf(new Random().nextInt(900000) + 100000);
            isExist.get().setOtp(otp);
            isExist.get().setOtpExpiry(LocalDateTime.now().plusMinutes(5));
            isExist.get().setVerified(false);

            otpSender.sedOtpMethod(login.getEmail(), otp);

            usersRepo.save(isExist.get());

            return new ResponseDto("Verification otp send to the mailId","NotVerified");
        }

    }

    public ResponseDto emailVerify(String email,String otp){

        Optional<Users> isExist = usersRepo.findByEmail(email);

        if(isExist.isEmpty()){
            throw new NotFound("This email doesn't exist");
        }
        else if(isExist.get().getOtpExpiry().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Otp Expired enter login details again");
        }
        else if(!isExist.get().getOtp().equals(otp)){
            throw new RuntimeException("Otp is not matching");
        }
        else{
            isExist.get().setVerified(true);
            isExist.get().setOtp(null);
            isExist.get().setOtpExpiry(null);

            usersRepo.save(isExist.get());

            String token = jwtUtil.generateJwtToken(isExist.get().getEmail());

            return new ResponseDto("Successfully Logged in","Verified",token);
        }
    }

    public ResponseDto getUserProfile(HttpServletRequest request){

        String pureToken = "";

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                pureToken = cookie.getValue();
            }
        }
        System.out.println("Token" + pureToken);
        String email = jwtUtil.decodeToken(pureToken).getSubject();
        System.out.println("profile" + email);
        Optional<Users> user = usersRepo.findByEmail(email);

        return new ResponseDto("User profile","Profile",user);
    }

    public ResponseDto editProfile(Users user){
        Optional<Users> fetch =  usersRepo.findByEmail(user.getEmail());

        fetch.get().setName(user.getName());
        fetch.get().setMobileNo(user.getMobileNo());

        usersRepo.save(fetch.get());

        return new ResponseDto("Successfully updated", "Updated");
    }

}
