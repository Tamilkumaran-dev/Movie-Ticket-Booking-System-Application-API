package com.app.MovieTicketBookingSystem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BookedSeats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "theatreId", description = "we have to pass the theatre id", defaultValue = "1")
    private Long theatreId;
    @Schema(name = "theatreName", description = "we have to pass the theatre name", defaultValue = "PVR")
    private String theatreName;
    @Schema(name = "theatreAddress", description = "we have to pass the theatre address", defaultValue = "chennai")
    private String theatreAddress;

    @Schema(name = "showId", description = "we have to pass the show id", defaultValue = "1")
    private Long showId;
    @Schema(name = "showName", description = "we have to pass the show name", defaultValue = "End game")
    private String showName;

    @Schema(name = "userId", description = "we have to pass the user id who booked the tickets", defaultValue = "1")
    private Long userId;
    @Schema(name = "userName", description = "we have to pass the user name who booked the tickets", defaultValue = "Tamil")
    private String userName;

    @Schema(name = "seats", description = "we have to pass the array of seats which the user booked", defaultValue = "[1,2,4]")
    private List<Integer> seats = new ArrayList<>();

    @Schema(name = "timing", description = "we have to pass the show timing ", defaultValue = "2025-10-13T21:30:00")
    private LocalDateTime timing;

    @Schema(name = "userData", description = "we have to pass we have to pass the whole user object who booked tickets", defaultValue = "User object")
    @ManyToOne
    @JoinColumn(name = "userData", referencedColumnName = "id")
    @JsonIgnore
    private Users userData;

    @Schema(name = "showData", description = "we have to pass we have to pass the whole show object", defaultValue = "Show object")
    @ManyToOne
    @JoinColumn(name= "showData", referencedColumnName = "id")
    @JsonIgnore
    private Shows showData;

}
