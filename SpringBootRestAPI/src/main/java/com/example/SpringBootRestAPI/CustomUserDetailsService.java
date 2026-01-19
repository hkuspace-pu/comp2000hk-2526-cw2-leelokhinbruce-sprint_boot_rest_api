package com.example.SpringBootRestAPI;

import com.example.SpringBootRestAPI.entity.User;
import com.example.SpringBootRestAPI.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

// UserDetailsService: used for loading/retrieving user-specific data by name or email from a backend data source
// and return an instance of the UserDetails interface
@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UserDetails obj: represents an auth user obj in the Spring Security framework
    // and contains sensitive data (user's username, password)
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Look up UserDetails for a given username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

        // Grant authority for the user based on the role
        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}