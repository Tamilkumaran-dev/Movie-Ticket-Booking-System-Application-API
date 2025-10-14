package com.app.MovieTicketBookingSystem.services;

import com.app.MovieTicketBookingSystem.entities.Shows;
import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.repositories.ShowsRepo;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HomeServicesTest {

    @Mock
    private TheatreRepo theatreRepo;

    @Mock
    private ShowsRepo showsRepo;

    @InjectMocks
    private HomeServices homeServices;

    private Theatre theatre;
    private Shows show;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        theatre = new Theatre();
        theatre.setId(1L);
        theatre.setTheatreName("PVR");
        theatre.setAddress("Chennai");

        show = new Shows();
        show.setId(1L);
        show.setTheatreId(1L);
        show.setShowName("End Game");
        show.setTiming(LocalDateTime.now().plusDays(1));
        show.setTheatre(theatre);
        theatre.setShows(List.of(show));
    }

    @Test
    void testGetAllTheatre() {
        when(theatreRepo.findAll()).thenReturn(List.of(theatre));

        List<Theatre> result = homeServices.getAllTheatre();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTheatreName()).isEqualTo("PVR");
        verify(theatreRepo, times(2)).findAll(); // called twice (due to your code)
    }

    @Test
    void testGetAllShows() {
        when(showsRepo.findAll()).thenReturn(List.of(show));

        List<Shows> result = homeServices.getAllShows();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSelectTheatre() {
        when(theatreRepo.findById(1L)).thenReturn(Optional.of(theatre));

        Theatre result = homeServices.selectTheatre(1L);

        assertThat(result.getTheatreName()).isEqualTo("PVR");
    }

    @Test
    void testSelectShow() {
        when(theatreRepo.findById(1L)).thenReturn(Optional.of(theatre));

        Shows result = homeServices.selectShow(1L, 1L);

        assertThat(result.getShowName()).isEqualTo("End Game");
    }

    @Test
    void testSearchTheatre_AllTheatre() {
        Page<Theatre> page = new PageImpl<>(List.of(theatre));
        when(theatreRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<Theatre> result = homeServices.searchTheatre("AllTheatre", 1, 5);

        assertThat(result.getContent()).hasSize(1);
        verify(theatreRepo, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testSearchTheatre_ByKeyword() {
        Page<Theatre> page = new PageImpl<>(List.of(theatre));
        when(theatreRepo.searchTheatre(eq("PVR"), any(Pageable.class))).thenReturn(page);

        Page<Theatre> result = homeServices.searchTheatre("PVR", 1, 5);

        assertThat(result.getContent().get(0).getTheatreName()).isEqualTo("PVR");
        verify(theatreRepo, times(1)).searchTheatre(eq("PVR"), any(Pageable.class));
    }

    @Test
    void testSearchShows_AllShows() {
        Page<Shows> page = new PageImpl<>(List.of(show));
        when(showsRepo.findAllUpcomingShows(any(Pageable.class))).thenReturn(page);

        Page<Shows> result = homeServices.searchShows("AllShows", 1, 5);

        assertThat(result.getContent()).hasSize(1);
        verify(showsRepo, times(1)).findAllUpcomingShows(any(Pageable.class));
    }

    @Test
    void testSearchShows_ByKeyword() {
        Page<Shows> page = new PageImpl<>(List.of(show));
        when(showsRepo.searchShows(eq("End Game"), any(Pageable.class))).thenReturn(page);

        Page<Shows> result = homeServices.searchShows("End Game", 1, 5);

        assertThat(result.getContent().get(0).getShowName()).isEqualTo("End Game");
        verify(showsRepo, times(1)).searchShows(eq("End Game"), any(Pageable.class));
    }
}
