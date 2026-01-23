package com.example.SpringBootRestAPI.service;

import io.jsonwebtoken.Claims;import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;import java.util.HashMap;import java.util.Map;import java.util.function.Function;

@Service
public class JwtTokenService {
    private static final long EXPIRATION_TIME = 86400000;  // 24 hrs
    // Replace with a secret key
    private final String SECRET_KEY = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=\r\n";

    // Secret key with decoder
    public Key getSingleKey() {
        byte[] getKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(getKey);
    }

    // Extract username from the token claims
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve the expiry from the token claims
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Get Claim from token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = getAllClaimFromToken(token);
        return claimsTFunction.apply(claims);
    }

    // Secret key will be required for retrieving data from token
    private Claims getAllClaimFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSingleKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check token for expiration
    private Boolean isTokenExpiration(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Generate new token during auth for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())  // Use email or username as subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // start date
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // end date
                .signWith(getSingleKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate incoming token for integrity
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }
}
