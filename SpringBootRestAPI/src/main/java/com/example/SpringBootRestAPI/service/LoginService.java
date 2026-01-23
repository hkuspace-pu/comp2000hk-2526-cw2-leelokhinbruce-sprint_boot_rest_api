package com.example.SpringBootRestAPI.service;

import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;import org.springframework.stereotype.Service;
import java.util.Collections;import java.util.Set;
import java.util.stream.Collectors;

// UserDetailsService: used for loading/retrieving user data from a backend data source
// Use the email or username to find user and return an instance of the UserDetails interface
// and injected into SecurityConfig
@Service
public class LoginService implements UserDetailsService{
    @Autowired
    private UserService userService;
//    private final UserRepository userRepository;

//    public LoginService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    // UserDetails obj: represents an auth user obj in the Spring Security framework
    // and contains sensitive data (user's username, password)
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Look up UserDetails for a given username or email
        User user = userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user != null) {
            // Return an instance of the UserDetails interface with these data (username, password, authorities)
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())))
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with username or email");
        }
//        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        // Grant authority for the user based on the role
//        Set<GrantedAuthority> authorities = user
//                .getRole()
//                .stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                authorities
//        );
    }
}