package com.example.SpringBootRestAPI.service;

import com.example.SpringBootRestAPI.model.Role;
import com.example.SpringBootRestAPI.model.Staff;
import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.repository.RoleRepository;
import com.example.SpringBootRestAPI.repository.StaffRepository;
import com.example.SpringBootRestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// A middle layer between the controller or security filter and the database
// Encapsulate the logic to fetch user details
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StaffRepository staffRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Staff> findByStaffId(String staffId) {
        return staffRepository.findById(staffId);
    }

    // Save and Flush data into the database
    public void saveAndFlush(User user) {
        userRepository.saveAndFlush(user);
    }

    // Save
    public void save(User user) {
        userRepository.save(user);
    }
}
