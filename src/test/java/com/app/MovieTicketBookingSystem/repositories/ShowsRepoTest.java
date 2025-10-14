package com.app.MovieTicketBookingSystem.repositories;

import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ShowsRepoTest {

    @Autowired
    private ShowsRepo showsRepo;

    @Autowired
    private TheatreRepo theatreRepo;

    private Theatre theatre;
    private Shows show1;
    private Shows show2;

    @BeforeEach
    void setup() {
        theatre = new Theatre();
        theatre.setTheatreName("PVR");
        theatre.setEmail("pvr@gmail.com");
        theatre.setPassword("pvr123");
        theatre.setAddress("Chennai");
        theatreRepo.save(theatre);

        show1 = new Shows();
        show1.setShowName("End Game");
        show1.setGenre("Action");
        show1.setTheatre(theatre);
        show1.setTheatreName("PVR");
        show1.setTheatreAddress("Chennai");
        show1.setTiming(LocalDateTime.now().plusDays(2)); // future show
        showsRepo.save(show1);

        show2 = new Shows();
        show2.setShowName("Avatar");
        show2.setGenre("Sci-Fi");
        show2.setTheatre(theatre);
        show2.setTheatreName("PVR");
        show2.setTheatreAddress("Chennai");
        show2.setTiming(LocalDateTime.now().minusDays(1)); // past show
        showsRepo.save(show2);
    }

    @Test
    void testSearchShows_ByName() {
        Page<Shows> result = showsRepo.searchShows("End Game", PageRequest.of(0, 5));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSearchShows_ByGenre() {
        Page<Shows> result = showsRepo.searchShows("Action", PageRequest.of(0, 5));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getGenre()).isEqualTo("Action");
    }

    @Test
    void testFindAllUpcomingShows() {
        Page<Shows> result = showsRepo.findAllUpcomingShows(PageRequest.of(0, 5));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSearchShows_NoFutureMatch() {
        Page<Shows> result = showsRepo.searchShows("Avatar", PageRequest.of(0, 5));
        assertThat(result.getContent()).isEmpty(); // show2 is past timing
    }
}
