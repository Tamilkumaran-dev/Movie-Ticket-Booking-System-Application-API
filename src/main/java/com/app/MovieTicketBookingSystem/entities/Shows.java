package com.app.MovieTicketBookingSystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long theatreId;
    private String theatreName;
    private String theatreAddress;
    private String showName;
    private String image;
    private LocalDateTime timing;

    private int ticketPrice;

    private int TotalSeats;

    @ElementCollection
    private SortedSet<Integer> availableSeats ;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatres", referencedColumnName = "id")
    @JsonIgnore
    private Theatre theatre;

    @OneToMany(mappedBy = "showData", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<BookedSeats> bookedSeats;


}
