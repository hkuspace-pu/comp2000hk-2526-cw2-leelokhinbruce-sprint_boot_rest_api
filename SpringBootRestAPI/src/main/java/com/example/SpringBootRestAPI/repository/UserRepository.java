package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// JpaRepository: used to interact with the database via JPA
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    // Find the user via finding user's email
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}