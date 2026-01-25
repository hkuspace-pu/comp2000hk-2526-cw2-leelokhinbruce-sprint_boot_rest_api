package com.example.SpringBootRestAPI.security;

import com.example.SpringBootRestAPI.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Define the Spring Security setup for the app
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationFilter authFilter;  // intercept and validate JWTs
    @Autowired
    private LoginService loginService;  // fetch user details from the database
//    private UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    // SecurityFilterChain: configs endpoint-level access control based on user roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF and CORS basic for stateless API, ensuring no token challenge for POST
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
//                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // for pre-flight
                                // Allow /api/auth/register & /api/auth/login to be accessed without authentication via permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                // Require authenticated before access
                                .requestMatchers("/api/guest/**").authenticated()
                                .anyRequest().authenticated()  // authorize the request
                )
                // Persist the auth user obj with the session
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Ensure the JWT filter runs before the default Spring login filter
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
//                .anonymous(Customizer.withDefaults());

        return http.build();
    }

    // Link the LoginService and password encoder to auth user properly
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(loginService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Auth logic during login
    // To finalize the config of auth manager
    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    // Hashing password
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
