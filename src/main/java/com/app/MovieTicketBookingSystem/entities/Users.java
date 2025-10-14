package com.app.MovieTicketBookingSystem.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "name", description = "We have to  pass the name", defaultValue = "Tamil")
    private String name;
    @Schema(name = "mobileNo", description = "We have to  pass the user mobile number", defaultValue = "123456789")
    private Long mobileNo;
    @Schema(name = "email", description = "We have to  pass the user email", defaultValue = "tamil@gmail.com")
    private String email;
    @Schema(name = "password", description = "We have to  pass the password for the account", defaultValue = "tamil")
    private String password;
    @Schema(name = "isVerified", description = "This used for account verification to check the email id is valid", defaultValue = "false")
    private boolean isVerified;
    @Schema(name = "otp", description = "This used for account verification using otp ", defaultValue = "1234")
    private String otp;
    @Schema(name = "otpExpiry", description = "This used as otp expiry time", defaultValue = "2025-10-13T21:30:00")
    private LocalDateTime otpExpiry;

    @Schema(name = "bookedTickets", description = "This used for connecting the BookedTickets table ", defaultValue = "list of BookedTickets object")
    @OneToMany(mappedBy = "userData", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<BookedSeats> bookedTickets;

    @Schema(name = "role", description = "This used for Authorization ", defaultValue = "ROLE_USER")
    private String role = "ROLE_USER";
}
