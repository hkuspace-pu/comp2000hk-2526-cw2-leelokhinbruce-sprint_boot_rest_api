package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;import java.util.Optional;

// JpaRepository: used to interact with the database via JPA
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
