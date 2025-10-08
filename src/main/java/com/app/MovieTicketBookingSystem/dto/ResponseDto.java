package com.app.MovieTicketBookingSystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private String message;
    private Boolean resStatus = true;
    private boolean isException = false;
    private String resKeyword;
    private String token;
    private Optional<?> data;

    public ResponseDto(String message) {
        this.message = message;
    }

    public ResponseDto(String message, String resKeyword) {
        this.message = message;
        this.resKeyword = resKeyword;
    }

    public ResponseDto(String message, String resKeyword,String token) {
        this.message = message;
        this.resKeyword = resKeyword;
        this.token = token;
    }
    public ResponseDto(String message, String resKeyword,Optional<?> data) {
        this.message = message;
        this.resKeyword = resKeyword;
        this.data = data;
    }
}
