package com.app.MovieTicketBookingSystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@EnableAsync
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Movie ticket booking system API",
		description = "In this Application i have implemented Two kind of user one is customer who book tickets, the other one is Theatre users who can add shows and sell tickets" +
				"I have implemented GET, POST, PUT and DELETE HTTP methods",
		version = "Beta 1.0.0",
		contact = @Contact(
				name = "Tamilkumaran",
				email = "Tamilkumaran021@gmail.com",
				url = ""
		)
))
public class MovieTicketBookingSystemApplication {

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


	public static void main(String[] args) {
		SpringApplication.run(MovieTicketBookingSystemApplication.class, args);
	}

}
