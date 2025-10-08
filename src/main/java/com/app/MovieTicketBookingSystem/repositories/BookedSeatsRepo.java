package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.BookedSeats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedSeatsRepo extends JpaRepository<BookedSeats,Long> {
}
