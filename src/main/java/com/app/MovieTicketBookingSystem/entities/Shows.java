package com.app.MovieTicketBookingSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@Data
@Entity
@Table(name = "shows")
@AllArgsConstructor
@NoArgsConstructor
public class Shows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(name = "theatreId", description = "we have to pass theatre Id which register the show", defaultValue = "1")
    private Long theatreId;

    @Schema(name = "theatreName", description = "we have to pass theatre name which register the show", defaultValue = "PVR")
    private String theatreName;

    @Schema(name = "theatreAddress", description = "we have to pass theatre address which register the show", defaultValue = "chennai")
    private String theatreAddress;

    @Schema(name = "showName", description = "we have to pass show name", defaultValue = "End game")
    private String showName;

    @Schema(name = "image", description = "we have to pass poster image url", defaultValue = "image url")
    private String image;

    @Schema(name = "image", description = "we have to pass poster image url", defaultValue = "image url")
    private String showDes;

    @Schema(name = "timing", description = "we have to pass timing", defaultValue = "2025-10-13T21:30:00")
    private LocalDateTime timing;

    @Schema(name = "genre", description = "we have to pass genre of the show", defaultValue = "Thriller")
    private String genre;

    @Schema(name = "duration", description = "we have to show duration", defaultValue = "2.5")
    private String duration;

    @Schema(name = "ticketPrice", description = "we have to show Ticket Price", defaultValue = "250")
    private int ticketPrice;

    @Schema(name = "TotalSeats", description = "we have to total seats of the show", defaultValue = "100")
    private int TotalSeats;


    @Schema(name = "availableSeats", description = "we have to pass the available seats for the show in array ", defaultValue = "[1,3,5,45,33,44,56,77]")
    @ElementCollection
    private SortedSet<Integer> availableSeats ;


    @Schema(name = "theatre", description = "The is used for connect the theatre table", defaultValue = "pass the Theatre object")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatres", referencedColumnName = "id")
    @JsonIgnore
    private Theatre theatre;

    @Schema(name = "showData", description = "The is used for connect the bookedSeats table", defaultValue = "pass the BookedSeats object")
    @OneToMany(mappedBy = "showData", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<BookedSeats> bookedSeats;

}
