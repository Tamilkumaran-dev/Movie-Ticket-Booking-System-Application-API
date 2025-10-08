package com.app.MovieTicketBookingSystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    private String message;
    private LocalDateTime timing;
    private String path;
    private int status;
    private Boolean isException = true;


}
