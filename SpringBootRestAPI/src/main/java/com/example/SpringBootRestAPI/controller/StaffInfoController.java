package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.dto.GuestDataRequest;
import com.example.SpringBootRestAPI.dto.StaffDataRequest;
import com.example.SpringBootRestAPI.model.Staff;
import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffInfoController {
    @Autowired
    private UserService userService;

    // Get staff data
    @GetMapping("/account_details")
    public ResponseEntity<?> getStaffData(Authentication auth) {
        try {
            // Get the auth user's username from the auth principal
            String username = auth.getName();
            // Fetch the current user by verifying username
            User user = userService.findByUsername(username);
            // Fetch staff data with valid id
            Optional<Staff> staffOpt = userService.findByStaffId(user.getId());
            if (staffOpt.isEmpty())
                return ResponseEntity.notFound().build();

            Staff staff = staffOpt.get();
            StaffDataRequest staffDataRequest = new StaffDataRequest(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    staff.getWorkingBranch(),
                    staff.getPosition()
            );
            return ResponseEntity.ok(staffDataRequest);  //Returns the obj
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get account details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update account details
    @PatchMapping("/account_details/edit")
    public ResponseEntity<String> editAccountDetails(@RequestBody StaffDataRequest staffDataRequest, Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.findByUsername(username);

            // Update staff data
            user.setPhoneNumber(staffDataRequest.getPhoneNumber());

            // Save the updated
            userService.saveAndFlush(user);

            return new ResponseEntity<>("Account details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update account details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
