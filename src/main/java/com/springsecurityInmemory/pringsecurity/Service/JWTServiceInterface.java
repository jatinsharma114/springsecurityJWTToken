package com.springsecurityInmemory.pringsecurity.Service;

import com.springsecurityInmemory.pringsecurity.DTO.AuthRequest;

public interface JWTServiceInterface {

    String generateJWTToken(String userName);
}
