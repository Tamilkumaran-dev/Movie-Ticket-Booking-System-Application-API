package com.app.MovieTicketBookingSystem.security;

import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilterChain jwtFilterChain) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/auth","/auth/**").permitAll()
                        .requestMatchers("/home","/home/**").permitAll()
                        .requestMatchers("/bookSeats","/bookSeats/**").hasRole("USER")
                        .requestMatchers("/user","/user/**").authenticated()
                        .requestMatchers("/theatre,/theatre/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}

