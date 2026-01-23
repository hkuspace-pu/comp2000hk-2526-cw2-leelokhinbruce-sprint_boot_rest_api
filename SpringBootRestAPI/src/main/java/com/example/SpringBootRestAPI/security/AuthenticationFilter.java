package com.example.SpringBootRestAPI.security;

import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.service.LoginService;
import com.example.SpringBootRestAPI.service.JwtTokenService;
import com.example.SpringBootRestAPI.repository.UserRepository;
import com.example.SpringBootRestAPI.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

// Runs before each incoming HTTP request
// Serve as a security filter
// Intercept and validate JWT tokens in each request
@Component
public class AuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");  // extracts the JWT from the auth header
        String filterToken = null;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            filterToken = header.substring(7);  // ignore first 7 chars
            // Get username from the token
            username = jwtTokenService.getUsernameFromToken(filterToken);
        }

        // Find user by the username of the token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByUsername(username);
            UserDetails userDetails = loginService.loadUserByUsername(user.getUsername());

            if (jwtTokenService.validateToken(filterToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Create an empty SecurityContext instead of using SecurityContextHolder.getContext().setAuthentication(authentication)
                // to avoid race conditions across multiple threads
                // SecurityContextHolder: Spring Security stores the details of who is authenticated
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                // Spring Security uses this info for authorization
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }
}
