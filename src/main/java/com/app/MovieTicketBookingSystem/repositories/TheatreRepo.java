package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TheatreRepo extends JpaRepository<Theatre,Long> {

    public Optional<Theatre> findByEmail(String email);






    @Query("""
           SELECT t FROM Theatre t 
           WHERE LOWER(t.theatreName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
              OR LOWER(t.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    Page<Theatre> searchTheatre(@Param("keyword") String keyword, Pageable pageable);
}
