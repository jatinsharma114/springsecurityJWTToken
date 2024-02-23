package com.springsecurityInmemory.pringsecurity.FilterJWT;

import com.springsecurityInmemory.pringsecurity.LoadUserByUsername.UserDetailsInformations;
import com.springsecurityInmemory.pringsecurity.Service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Using OncePerRequestFilter for Synchronous Requests
 * only supports >> HTTP requests
 */


/**
 * Before Use any filter Use this JWTFilter
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsInformations userDetailsInformations;

    @Autowired
    public JWTFilter(JWTService jwtService, UserDetailsInformations userDetailsInformations) {
        this.jwtService = jwtService;
        this.userDetailsInformations = userDetailsInformations;
    }

    /**
     * Http Request ::
     * Called : Authentication Filter
     * Authentication Manager ==> Provider manager
     * Provider manager  ---> Authentication provider 1
     *                          ---> Authentication provider 2
     *
     * Authentication provider ---->  UserDetails ----> Fetch the Data from the DB
     * UserDetails -----> Provider manager
     * Provider manager ---> Authenticated Object hota hai => Attached to the SecurityContextHolder.
     *
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         *  Token s extract kerna hai
         *   sername for validation :: USerDetails k pass s
         *    alidation for the DB object
         *    TOKEN ::: expiration check
         *
         *    ENd : add in the :: SecurityContextHolder - for the repose to the client
         */

        String informationInJWTToken = request.getHeader("Authorization");

        String token = null;
        String username = null; //------- UserDetails we Fetch For Validation

        if (informationInJWTToken != null && informationInJWTToken.startsWith("Bearer ")) {
            token = informationInJWTToken.substring(7);
            username = jwtService.extractUsername(token);
        }

        /**
         * SecurityContextHolder.getContext().getAuthentication()
         *
         * IF not null then It already holding the Athenticated object
         * Object which is authenticated Using the UserDetails - Service say..
         *
         */

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // User which I have inserted here
            UserDetails userDetails = userDetailsInformations.loadUserByUsername(username);// FROM DB

            // UserName & Token = validation hai toh ::
            if (jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // End m YE Authentipcation ka Object hold kerta hai internally
                // SO I need to set this here
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
