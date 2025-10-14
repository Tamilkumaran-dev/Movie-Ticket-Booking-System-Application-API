package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.services.HomeServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HomeControllerTest {

    @Mock
    private HomeServices homeServices;

    @InjectMocks
    private HomeController homeController;

    private Theatre theatre;
    private Shows show;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        theatre = new Theatre();
        theatre.setId(1L);
        theatre.setTheatreName("PVR");

        show = new Shows();
        show.setId(1L);
        show.setShowName("End Game");
        show.setTiming(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testAllTheatre() {
        when(homeServices.getAllTheatre()).thenReturn(List.of(theatre));

        ResponseEntity<List<Theatre>> response = homeController.allTheatre();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testAllShows() {
        when(homeServices.getAllShows()).thenReturn(List.of(show));

        ResponseEntity<List<Shows>> response = homeController.allShows();

        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSelectTheatre() {
        when(homeServices.selectTheatre(1L)).thenReturn(theatre);

        ResponseEntity<Theatre> response = homeController.selectTheatre(1L);

        assertThat(response.getBody().getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testSelectShow() {
        when(homeServices.selectShow(1L, 1L)).thenReturn(show);

        ResponseEntity<Shows> response = homeController.selectShow(1L, 1L);

        assertThat(response.getBody().getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSearchTheatre() {
        Page<Theatre> page = new PageImpl<>(List.of(theatre));
        when(homeServices.searchTheatre("PVR", 1, 5)).thenReturn(page);

        ResponseEntity<Page<Theatre>> response = homeController.searchTheatre("PVR", 1, 5);

        assertThat(response.getBody().getContent().get(0).getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testSearchShows() {
        Page<Shows> page = new PageImpl<>(List.of(show));
        when(homeServices.searchShows("End Game", 1, 5)).thenReturn(page);

        ResponseEntity<Page<Shows>> response = homeController.searchShows("End Game", 1, 5);

        assertThat(response.getBody().getContent().get(0).getShowName()).isEqualTo("End Game");
    }
}
