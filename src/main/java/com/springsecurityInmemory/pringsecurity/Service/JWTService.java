package com.springsecurityInmemory.pringsecurity.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTService implements JWTServiceInterface{
    private static final String SECRET= "4aab8c18c308f97d5d39001e78ce6e5d7d6c8220050ce0a48cfadd419b5e7f4e";

    // Generating the Key ::::::::::::::::::::::::::::::::::::::::::::
    @Override
    public String generateJWTToken(String userName) {
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))//30min
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::;



    /** Below are predefined method :: used kra to extract the Claims from jwtToken.
     * --------------------------------------------------------------------------------------
     */
    // Claims nothing butt a 3 types stuffs which create a JWT token Header /Payload(DATA) /Signature{SECRET)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // VValidation phase in JWT :: Filter :::::::::::::::::::::::::::::::::::::::
    /**
     * // Checking IF TOKEN + userName == UserDetails ( fetched from the DB )
     * // UserNAME & Token Expire Time
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



}
