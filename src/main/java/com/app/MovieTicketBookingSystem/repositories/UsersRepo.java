package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users,Long> {

    public Optional<Users> findByEmail(String email);

}
