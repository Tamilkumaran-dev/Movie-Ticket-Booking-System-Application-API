package com.app.MovieTicketBookingSystem.entities;

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
    private String name;
    private Long mobileNo;
    private String email;
    private String password;
    private boolean isVerified;

    private String otp;
    private LocalDateTime otpExpiry;


    @OneToMany(mappedBy = "userData", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<BookedSeats> bookedTickets;

    private String role = "ROLE_USER";
}
