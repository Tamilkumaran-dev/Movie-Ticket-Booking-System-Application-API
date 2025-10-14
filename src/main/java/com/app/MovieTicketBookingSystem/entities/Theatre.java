package com.app.MovieTicketBookingSystem.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "theatreName", description = "We have to  pass the theatre name", defaultValue = "PVR")
    private String theatreName;
    @Schema(name = "email", description = "We have to  pass the theatre email id ", defaultValue = "tamil@gmail.com")
    private String email;
    @Schema(name = "password", description = "We have to  pass the password for the theatre account ", defaultValue = "tamil12345")
    private String password;
    @Schema(name = "address", description = "We have to  pass the address of the theatre", defaultValue = "chennai")
    private String address;

    @Schema(name = "isVerified", description = "This used for account verification to check the email id is valid", defaultValue = "false")
    private boolean isVerified;

    @Schema(name = "otp", description = "This used for account verification using otp ", defaultValue = "1234")
    private String otp;

    @Schema(name = "otpExpiry", description = "This used as otp expiry time", defaultValue = "2025-10-13T21:30:00")
    private LocalDateTime otpExpiry;

    @Schema(name = "shows", description = "This used for connecting the show table", defaultValue = "array of show object")
    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Shows> shows = new ArrayList<>();

    @Schema(name = "role", description = "This used for Authorization ", defaultValue = "ROLE_THEATRE")
    private String role = "ROLE_THEATRE";

}
