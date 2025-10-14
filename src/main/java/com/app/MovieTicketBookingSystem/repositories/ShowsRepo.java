package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.Shows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowsRepo extends JpaRepository<Shows,Long> {


    @Query("""
   SELECT s FROM Shows s
   WHERE (
      LOWER(s.showName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      OR LOWER(s.theatreName) LIKE LOWER(CONCAT('%', :keyword, '%'))
      OR LOWER(s.theatreAddress) LIKE LOWER(CONCAT('%', :keyword, '%'))
      OR LOWER(s.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))
   )
   AND s.timing > CURRENT_TIMESTAMP
   """)
    Page<Shows> searchShows(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT s FROM Shows s WHERE s.timing > CURRENT_TIMESTAMP")
    Page<Shows> findAllUpcomingShows(Pageable pageable);

}
