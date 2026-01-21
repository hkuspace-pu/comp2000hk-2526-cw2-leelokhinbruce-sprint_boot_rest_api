package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.CustomUserDetailsService;
import com.example.SpringBootRestAPI.JwtTokenService;
import com.example.SpringBootRestAPI.dto.LoginRequest;
import com.example.SpringBootRestAPI.dto.RegisterRequest;
import com.example.SpringBootRestAPI.entity.Role;
import com.example.SpringBootRestAPI.entity.User;
import com.example.SpringBootRestAPI.repository.RoleRepository;
import com.example.SpringBootRestAPI.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtTokenService jwtService;

    // SecurityContextLogoutHandler(): invalidate session and clear the security context
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @org.springframework.beans.factory.annotation.Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Login | Sign in
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

            // Create an empty SecurityContext instead of using SecurityContextHolder.getContext().setAuthentication(authentication)
            // to avoid race conditions across multiple threads
            // SecurityContextHolder: Spring Security stores the details of who is authenticated
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);

            // Spring Security uses this info for authorization
            SecurityContextHolder.setContext(context);

            String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

            return new ResponseEntity<>(token, HttpStatus.OK);
//            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Register | Sign up
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Check for username exists in a DB
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return new ResponseEntity<>("Username is already used!", HttpStatus.BAD_REQUEST);
            }
            // Check for email exists in a DB
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return new ResponseEntity<>("Email is already used!", HttpStatus.BAD_REQUEST);
            }

            // Create user obj
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setGender(registerRequest.getGender());

            Optional<Role> roles = roleRepository.findByName("ROLE_USER");

            if (roles.isEmpty())
                return new ResponseEntity<>("ROLE_USER not found. Contact admin", HttpStatus.INTERNAL_SERVER_ERROR);
            Role userRole = roles.get();

            user.setRoles(Set.of(userRole));
            userRepository.save(user);  // Save the user into the DB

            // Generate JWT token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            String token = jwtService.generateToken(userDetails);

            return new ResponseEntity<>(token, HttpStatus.OK);  // Return token
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutHandler.logout(request, response, authentication);

        // Invalidate session
        request.getSession().invalidate();
        // Clear SecurityContext from the current thread
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>("Logged out successfully!", HttpStatus.OK);
    }
}
