package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// JpaRepository: used to interact with the database via JPA,
// enabling build-in CRUD operations for user entities
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Custom query methods
    // Find the user via finding user's email
    User findByEmail(String email);

    User findByUsername(String username);

    User findByUsernameOrEmail(String username, String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}