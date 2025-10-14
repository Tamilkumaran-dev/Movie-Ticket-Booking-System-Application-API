package com.app.MovieTicketBookingSystem.utils;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@AllArgsConstructor
public class OtpSender {

    private JavaMailSender mailSender;

    @Async
    public void sedOtpMethod(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email verification otp");
        message.setText("Your OTP code is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
        mailSender.send(message);
    }

    @Async
    public void ticketConformationMethod(String email, String theatre, String address,String showName, LocalDateTime time, String seats){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ticket Conformed!");
        message.setText("Ticket conformed " +"\nTheatre : " + theatre + "\nAddress : " + address + "\nMovie : " + showName + "\nTiming : " + time + "SeatNo : " + seats);
        mailSender.send(message);
    }

    @Async
    public void cancelTicketMethod(String email,String showName, LocalDateTime time, String seats){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Ticket Cancel!");
        message.setText("Ticket Cancel " +"\nMovie : "  + showName  + "\nTiming : " + time + "SeatNo : " + seats);
        mailSender.send(message);
    }



}
