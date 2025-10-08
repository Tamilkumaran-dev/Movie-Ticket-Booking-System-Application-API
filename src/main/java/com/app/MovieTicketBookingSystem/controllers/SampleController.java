package com.app.MovieTicketBookingSystem.controllers;

import com.app.MovieTicketBookingSystem.entities.SampleTable;
import com.app.MovieTicketBookingSystem.repositories.SampleRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SampleController {

    private SampleRepo sampleRepo;

    @GetMapping("sample")
    public String Hellomethod(){
        return "Hello";
    }

    @PostMapping("addArray")
    public SampleTable addData(@RequestBody SampleTable sampleTable){
        SampleTable st = sampleRepo.save(sampleTable);
        System.out.println(st.getSampleArray().get(1));
        return sampleRepo.save(sampleTable);
    }
}
