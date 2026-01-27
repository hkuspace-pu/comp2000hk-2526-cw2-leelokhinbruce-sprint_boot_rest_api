package com.example.SpringBootRestAPI.repository;

import com.example.SpringBootRestAPI.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String>{
    // Get entire data by id
    Optional<Staff> findById(String id);
}
