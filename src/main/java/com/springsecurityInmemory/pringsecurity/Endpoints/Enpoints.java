package com.springsecurityInmemory.pringsecurity.Endpoints;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Enpoints {

    @GetMapping("/welcome")
    public String welcome(){
        return "Unsecure ::::::: ";
    }

    @GetMapping("/everything")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String forAdmin(){
        return "For admin";
    }

    @GetMapping("/product")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String user(){
        return "For USer";
    }


}
