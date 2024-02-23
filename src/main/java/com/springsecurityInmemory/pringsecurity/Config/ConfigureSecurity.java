package com.springsecurityInmemory.pringsecurity.Config;

import com.springsecurityInmemory.pringsecurity.FilterJWT.JWTFilter;
import com.springsecurityInmemory.pringsecurity.LoadUserByUsername.UserDetailsInformations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @EnableWebSecurity //(Filter chain) ALl the filter and configurations and imported class will execute while booting the app.
 *
 * @EnableMethodSecurity // Method Level = @PreAuthorize(
 */
@Configuration
@EnableWebSecurity //(Filter chain) ALl the filter and configurations and imported class will execute while booting the app.
@EnableMethodSecurity // Method Level = @PreAuthorize(
public class ConfigureSecurity {

    @Autowired
    JWTFilter jwtFilter;


    //Authentication
    @Bean
    public UserDetailsService userDetails() {
        System.out.println("userDetails ::::::");
//        new User();
//        UserDetails admin = User.withUsername("ms")
//                .password(passwordEncoder.encode("ms"))
//                .roles("ADMIN") // GrantedAuthority -> SImpl Object insert >> GrantedAuthority
//                .build();
//
//        UserDetails user = User.withUsername("jd")
//                .password(passwordEncoder.encode("jd"))
//                .roles("USER")
//                .build();
        UserDetailsService users = new UserDetailsInformations();
        return users;
//        return new UserDetailsInformations();
//        InMemoryUserDetailsManager inMemoryUserDetailsManager  = new InMemoryUserDetailsManager(admin, user);
//        return inMemoryUserDetailsManager;
    }

    //Authorization

    /**
     * ALl the FIlter chain will work on and
     * HttpSecurity -> Any rquest come for the authentication ::
     *  -----> object allows you to configure various aspects
     *                  - authentication,
     *                      authorization,
     *                      session management,
     *                      CSRF protection( deprecated ), and more.
     *
     * securityFilterChain method is used to define the
     * security configuration for a specific set of HTTP requests.
     *
     * Security Filter chain configuration stuffs ::::>>>
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("securityFilterChain ::::::");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)// because Spring internally invoke this SO need to disable If we don't needed.
                .authorizeRequests()
                .requestMatchers("/welcome").permitAll() // Allow all the request for this Endpoint
                .requestMatchers("/getJWTToken").permitAll()
                .requestMatchers("/addUser").permitAll()
                .requestMatchers("/everything/**").authenticated() // Nay endpoint after /lg
                .requestMatchers("/product/**").authenticated()
                .and()
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetails());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // JWT :
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * AuthenticationManager manages ::: different type of AuthenticationProvider
     * AuthenticationProvider -----> DaoAuthenticationProvider
     *
     */



}
