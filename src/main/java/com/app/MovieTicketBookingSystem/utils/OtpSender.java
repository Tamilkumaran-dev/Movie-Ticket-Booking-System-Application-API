//package com.app.MovieTicketBookingSystem.utils;
//
//import lombok.AllArgsConstructor;
//import lombok.experimental.UtilityClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//
//@Component
//@AllArgsConstructor
//public class OtpSender {
//
//    private JavaMailSender mailSender;
//
//    @Async
//    public void sedOtpMethod(String email, String otp){
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Email verification otp");
//        message.setText("Your OTP code is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
//        mailSender.send(message);
//    }
//
//    @Async
//    public void ticketConformationMethod(String email, String theatre, String address,String showName, LocalDateTime time, String seats){
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Ticket Conformed!");
//        message.setText("Ticket conformed " +"\nTheatre : " + theatre + "\nAddress : " + address + "\nMovie : " + showName + "\nTiming : " + time + "SeatNo : " + seats);
//        mailSender.send(message);
//    }
//
//    @Async
//    public void cancelTicketMethod(String email,String showName, LocalDateTime time, String seats){
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Ticket Cancel!");
//        message.setText("Ticket Cancel " +"\nMovie : "  + showName  + "\nTiming : " + time + "SeatNo : " + seats);
//        mailSender.send(message);
//    }
//
//
//
//}


package com.app.MovieTicketBookingSystem.utils;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class OtpSender {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_SENDER}")
    private String senderEmail;

    private void sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(senderEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("SendGrid Status Code: " + response.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email via SendGrid");
        }
    }

    @Async
    public void sedOtpMethod(String email, String otp) {
        String subject = "Email Verification OTP";
        String body = "Your OTP code is: " + otp + "\n\nThis OTP will expire in 5 minutes.";
        sendEmail(email, subject, body);
    }

    @Async
    public void ticketConformationMethod(String email, String theatre, String address, String showName, LocalDateTime time, String seats) {
        String subject = "Ticket Confirmed!";
        String body = "Ticket confirmed\nTheatre: " + theatre +
                "\nAddress: " + address +
                "\nMovie: " + showName +
                "\nTiming: " + time +
                "\nSeats: " + seats;
        sendEmail(email, subject, body);
    }

    @Async
    public void cancelTicketMethod(String email, String showName, LocalDateTime time, String seats) {
        String subject = "Ticket Cancelled!";
        String body = "Ticket Cancelled\nMovie: " + showName +
                "\nTiming: " + time +
                "\nSeats: " + seats;
        sendEmail(email, subject, body);
    }
}
