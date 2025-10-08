package com.app.MovieTicketBookingSystem.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theatres")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String theatreName;
    private String email;
    private String password;
    private String address;
    private boolean isVerified;

    private String otp;
    private LocalDateTime otpExpiry;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Shows> shows = new ArrayList<>();

    private String role = "ROLE_THEATRE";

}
