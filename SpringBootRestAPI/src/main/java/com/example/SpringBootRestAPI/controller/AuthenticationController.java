package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.service.LoginService;
import com.example.SpringBootRestAPI.service.JwtTokenService;
import com.example.SpringBootRestAPI.dto.LoginRequest;
import com.example.SpringBootRestAPI.dto.RegisterRequest;
import com.example.SpringBootRestAPI.model.Role;
import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.repository.RoleRepository;
import com.example.SpringBootRestAPI.repository.UserRepository;
import com.example.SpringBootRestAPI.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;  // created in security config
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginService loginService;  // handle login activities
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenService jwtService;  // handle JWT token methods

    // SecurityContextLogoutHandler(): invalidate session and clear the security context
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    // Login | Sign in
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Create auth obj with auth manager
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

            // Fetch user details with username or email
            UserDetails userDetails = loginService.loadUserByUsername(loginRequest.getUsernameOrEmail());

            if (auth.isAuthenticated()) {
                // Generate token for the authentication user
                String access_token = jwtService.generateToken(userDetails);
                User user = userService.findByUsername(userDetails.getUsername());
                String role = user.getRole().getName();  // e.g. ROLE_GUEST or ROLE_STAFF

                Map<String, String> response = new HashMap<>();
                response.put("access_token", access_token);
                response.put("role", role);
                return new ResponseEntity<>(response, HttpStatus.OK);  // Returns token and role
            }
            return new ResponseEntity<>("Login failed: ", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Login error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Register | Sign up
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Check for username exists in a DB
            if (userService.existsByUsername(registerRequest.getUsername())) {
                return new ResponseEntity<>("Username is already used!", HttpStatus.BAD_REQUEST);
            }
            // Check for email exists in a DB
            if (userService.existsByEmail(registerRequest.getEmail())) {
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

            // Apply "Guest" role to the user
            Optional<Role> roles = userService.findByName("ROLE_GUEST");
            Role userRole = roles.get();

            user.setRole(userRole);
            userRepository.save(user);  // Save the user into the DB

            // Generate JWT token
            UserDetails userDetails = loginService.loadUserByUsername(user.getEmail());
            String access_token = jwtService.generateToken(userDetails);
            Map<String, String> response = new HashMap<>();
            response.put("access_token", access_token);
            return new ResponseEntity<>(response, HttpStatus.OK);  // Returns token
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
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
