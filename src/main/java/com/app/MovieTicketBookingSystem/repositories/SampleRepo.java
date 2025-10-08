package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.SampleTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepo extends JpaRepository<SampleTable,Long> {

}
