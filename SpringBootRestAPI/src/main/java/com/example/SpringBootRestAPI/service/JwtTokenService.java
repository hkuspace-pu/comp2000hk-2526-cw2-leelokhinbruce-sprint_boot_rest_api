package com.example.SpringBootRestAPI.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;import java.util.HashMap;import java.util.Map;

@Service
public class JwtTokenService {
    private static final long EXPIRATION_TIME = 86400000;  // 24 hrs
    // Replace with a secret key
    private final String SECRET_KEY = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=\\r\\n";

    public Key getSingleKey() {
        byte[] getKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(getKey);
    }

    // Generate new token during auth
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())  // Use email or username as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // start date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // end date
//                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .signWith(getSingleKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from the token claims
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate incoming token for integrity
    public boolean validateToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(getUsernameFromToken(token)) && !isTokenExpiration(token));
    }

    // Check token for expiration
    private boolean isTokenExpiration(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
