package com.genc.springsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    public static final String SECRET = "a8F9kLmN2PqR7xYv4BcD8eGhJ1KsW6ZnM3RtU5iOp9QwX2eRf";

    public Key getSignedKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .addClaims(new HashMap<>())
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String genToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .addClaims(new HashMap<>())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Claims verifySignatureAndExtractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return verifySignatureAndExtractAllClaims(token).getSubject();
    }

    public Date getExpiration(String token) {
        return verifySignatureAndExtractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}