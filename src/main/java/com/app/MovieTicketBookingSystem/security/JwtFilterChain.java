package com.app.MovieTicketBookingSystem.security;

import com.app.MovieTicketBookingSystem.entities.Theatre;
import com.app.MovieTicketBookingSystem.entities.Users;
import com.app.MovieTicketBookingSystem.exception.exceptions.NotFound;
import com.app.MovieTicketBookingSystem.repositories.TheatreRepo;
import com.app.MovieTicketBookingSystem.repositories.UsersRepo;
import com.app.MovieTicketBookingSystem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtFilterChain extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UsersRepo usersRepo;
    private TheatreRepo theatreRepo;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        String pureToken = "";

        if(path.startsWith("/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        if(path.startsWith("/home")){
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("jwt")){
                    pureToken = cookie.getValue();
                    try {
                        System.out.println("Cookie token" + pureToken);
                        if(jwtUtil.isValid(pureToken)){
                            Claims decodeToken = jwtUtil.decodeToken(pureToken);

                            String email = decodeToken.getSubject();

                            Optional<Users> users = usersRepo.findByEmail(email);
                            Optional<Theatre> theatre = theatreRepo.findByEmail(email);

                            if(users.isPresent()){
                                String role = users.get().getRole();
                                Set<String> roles = new HashSet<>(List.of(role));

                                Set<GrantedAuthority> grantedAuthorities = roles.stream().map((r)->new SimpleGrantedAuthority(r)).collect(Collectors.toSet());

                                UsernamePasswordAuthenticationToken generateToken = new UsernamePasswordAuthenticationToken(users.get().getEmail(),users.get().getPassword(),grantedAuthorities);

                                SecurityContextHolder.getContext().setAuthentication(generateToken);
                            }

                            else if(theatre.isPresent()){

                                String role = theatre.get().getRole();
                                Set<String> roles = new HashSet<>(List.of(role));

                                Set<GrantedAuthority> grantedAuthorities = roles.stream().map((r)->new SimpleGrantedAuthority(r)).collect(Collectors.toSet());

                                UsernamePasswordAuthenticationToken generateToken = new UsernamePasswordAuthenticationToken(theatre.get().getEmail(),theatre.get().getPassword(),grantedAuthorities);

                                SecurityContextHolder.getContext().setAuthentication(generateToken);
                            }

                            else{
                                throw new NotFound("No user Found");
                            }

                        }
                    }catch(io.jsonwebtoken.ExpiredJwtException ex) {

                        clearJwtCookie(response);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Token expired, please login again");
                        return;
                    }catch (io.jsonwebtoken.JwtException ex) {
                        // Invalid token → clear cookie too
                        clearJwtCookie(response);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid token");
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request,response);
    }

    private void clearJwtCookie(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("jwt", null);
        expiredCookie.setPath("/");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setMaxAge(0); // delete immediately
        response.addCookie(expiredCookie);
    }
}
