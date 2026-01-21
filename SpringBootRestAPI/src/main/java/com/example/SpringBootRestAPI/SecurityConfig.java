package com.example.SpringBootRestAPI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;import org.springframework.web.cors.CorsConfiguration;import org.springframework.web.cors.CorsConfigurationSource;import org.springframework.web.cors.UrlBasedCorsConfigurationSource;import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
        private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Authenticate with username and password
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disable CSRF and HTTP basic, ensuring no token challenge for POST
                .csrf(AbstractHttpConfigurer::disable)
                // default 401 unauthorized response
//                .exceptionHandling(exceptionHandling ->
//                        // authenticationEntryPoint(): overrides default error of 500
//                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // for pre-flight
                                .requestMatchers(HttpMethod.GET, "/public_resource").permitAll()
                                // Allow /api/auth/register & /api/auth/login to be accessed without authentication via permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()  // authorize the request
                )
                // Persist the auth user obj with the session
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .anonymous(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Hashing password
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
