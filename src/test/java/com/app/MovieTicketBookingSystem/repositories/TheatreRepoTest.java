package com.app.MovieTicketBookingSystem.repositories;

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
class TheatreRepoTest {

    @Autowired
    private TheatreRepo theatreRepo;

    private Theatre theatre1;
    private Theatre theatre2;

    @BeforeEach
    void setup() {
        theatre1 = new Theatre();
        theatre1.setTheatreName("PVR");
        theatre1.setEmail("pvr@gmail.com");
        theatre1.setPassword("pvr123");
        theatre1.setAddress("Chennai");
        theatre1.setOtp("1111");
        theatre1.setOtpExpiry(LocalDateTime.now().plusDays(1));
        theatreRepo.save(theatre1);

        theatre2 = new Theatre();
        theatre2.setTheatreName("Inox");
        theatre2.setEmail("inox@gmail.com");
        theatre2.setPassword("inox123");
        theatre2.setAddress("Bangalore");
        theatre2.setOtp("2222");
        theatre2.setOtpExpiry(LocalDateTime.now().plusDays(1));
        theatreRepo.save(theatre2);
    }

    @Test
    void testFindByEmail() {
        var theatre = theatreRepo.findByEmail("pvr@gmail.com");
        assertThat(theatre).isPresent();
        assertThat(theatre.get().getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testSearchTheatre_ByName() {
        Page<Theatre> result = theatreRepo.searchTheatre("PVR", PageRequest.of(0, 5));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testSearchTheatre_ByAddress() {
        Page<Theatre> result = theatreRepo.searchTheatre("Bangalore", PageRequest.of(0, 5));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTheatreName()).isEqualTo("Inox");
    }

    @Test
    void testSearchTheatre_NoMatch() {
        Page<Theatre> result = theatreRepo.searchTheatre("Delhi", PageRequest.of(0, 5));
        assertThat(result.getContent()).isEmpty();
    }
}
