package com.example.SpringBootRestAPI;

import com.example.SpringBootRestAPI.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;import io.jsonwebtoken.security.Keys;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;import java.util.Map;
import java.util.Objects;

@Service
public class JwtTokenService {
    private static final String SECRET_KEY = "c29tZV9yYW5kb21fYmFzZTY0X2tleV9zdHJpbmdfbG9uZ19lbm91Z2g=";  // Replace with a secret key
    private static final long EXPIRATION_TIME = 86400000;  // 24 hrs



    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())  // Use email or username as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
