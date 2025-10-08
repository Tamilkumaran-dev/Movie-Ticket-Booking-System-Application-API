package com.app.MovieTicketBookingSystem.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Long theatreId;

    private Long showId;

    private Long userId;

    private List<Integer> seats = new ArrayList<>();

    private LocalDateTime timing;

    @ManyToOne
    @JoinColumn(name = "userData", referencedColumnName = "id")
    @JsonIgnore
    private Users userData;

    @ManyToOne
    @JoinColumn(name= "showData", referencedColumnName = "id")
    @JsonIgnore
    private Shows showData;

}
