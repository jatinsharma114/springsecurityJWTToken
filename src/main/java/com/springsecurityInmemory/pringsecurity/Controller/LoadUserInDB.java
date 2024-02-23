package com.springsecurityInmemory.pringsecurity.Controller;


import com.springsecurityInmemory.pringsecurity.DTO.AuthRequest;
import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import com.springsecurityInmemory.pringsecurity.Request.LoadUserRequest;
import com.springsecurityInmemory.pringsecurity.Service.JWTService;
import com.springsecurityInmemory.pringsecurity.Service.LoadUSerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadUserInDB {
    @Autowired
    LoadUSerService loadUSerService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTService jwtService;
    @PostMapping("/addUser")
    public String addUser(@RequestBody LoadUserRequest userRequest){
        UserInfo authRequest = loadUSerService.addUser(userRequest.to(userRequest));

        Authentication authenticate = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(userRequest.getUserName(),userRequest.getUserPassword()));
        if(authenticate.isAuthenticated()){
                return jwtService.generateJWTToken(authRequest.getUserName());
        } else {
                throw new UsernameNotFoundException("User not found in the DB");
        }
    }

    //ByPAss in Filter Chain
    // Before generating the Token >> Checking User hai ya ni.
    // Fetch from the UserDetails
    // verfiyUserBeforeGeneratingTheToken :::::::::::::::::::::::::::::::::::;
    /**
     * @UsernamePasswordAuthenticationToken
     *
     * Provider manager to :: go each n evrry authentication-provider
     * process of authentication :-
     * DaoAuthentication provider :: UsernamePasswordAuthenticationToken
     * We are doing the User and password based yaha per ::
     * @param authRequest
     * @return
     */
//    @GetMapping("/getJWTToken")
//    public String getJWTToken(@RequestBody AuthRequest authRequest){
//        Authentication authenticate =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getUserName()));
//        if(authenticate.isAuthenticated()){
//            return jwtService.generateJWTToken(authRequest.getUserName());
//        } else {
//            throw new UsernameNotFoundException("User not found in the DB");
//        }
//    }

}
