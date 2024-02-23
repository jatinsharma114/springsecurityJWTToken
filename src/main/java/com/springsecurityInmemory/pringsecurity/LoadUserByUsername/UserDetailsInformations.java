package com.springsecurityInmemory.pringsecurity.LoadUserByUsername;

import com.springsecurityInmemory.pringsecurity.Dao.UserRepository;
import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import com.springsecurityInmemory.pringsecurity.DTO.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * public class InMemoryUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {
 *
 *
 *  UserDetailsManager -------> UserDetailsService
 *  public interface UserDetailsManager extends UserDetailsService {
 */
@Component
public class UserDetailsInformations implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    // Flow :::>>
    //When we hit the API this method is being invoked internally. loadUserByUsername
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username ::::::::::: " + username);
        Optional<UserInfo> users = userRepository.findByUserName(username); // User from DB => ms
        UserDetails userDetails = users.map(x -> new UserInfoDTO(x))
                                                    .orElseThrow(() -> new RuntimeException("User not found"));
        return userDetails;
    }


}
