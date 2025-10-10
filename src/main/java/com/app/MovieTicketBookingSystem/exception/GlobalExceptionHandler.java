package com.app.MovieTicketBookingSystem.exception;

import com.app.MovieTicketBookingSystem.exception.exceptions.DataAlreadyExistException;
import com.app.MovieTicketBookingSystem.exception.exceptions.InputFieldIsEmpty;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<ExceptionDto> dataIsAlreadyExist( DataAlreadyExistException thrownException, WebRequest webRequest){

        ExceptionDto exception = new ExceptionDto();
        exception.setMessage(thrownException.getMessage());
        exception.setTiming(LocalDateTime.now());
        exception.setStatus(HttpStatus.BAD_REQUEST.value());
        exception.setPath(webRequest.getDescription(false));

        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InputFieldIsEmpty.class)
    public ResponseEntity<ExceptionDto> inputFieldIsEmpty(InputFieldIsEmpty thrownException, WebRequest webRequest){

        ExceptionDto exception = new ExceptionDto();
        exception.setMessage(thrownException.getMessage());
        exception.setTiming(LocalDateTime.now());
        exception.setStatus(HttpStatus.BAD_REQUEST.value());
        exception.setPath(webRequest.getDescription(false));

        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ExceptionDto> UserNotFound(NotFound thrownException, WebRequest webRequest){

        ExceptionDto exception = new ExceptionDto();
        exception.setMessage(thrownException.getMessage());
        exception.setTiming(LocalDateTime.now());
        exception.setStatus(HttpStatus.NOT_FOUND.value());
        exception.setPath(webRequest.getDescription(false));

        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> defaultException(Exception thrownException, WebRequest webRequest){

        ExceptionDto exception = new ExceptionDto();
        exception.setMessage(thrownException.getMessage());
        exception.setTiming(LocalDateTime.now());
        exception.setStatus(HttpStatus.CONFLICT.value());
        exception.setPath(webRequest.getDescription(false));

        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }

}
