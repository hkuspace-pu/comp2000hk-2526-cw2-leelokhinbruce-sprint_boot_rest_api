package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.dto.GuestDataRequest;
import com.example.SpringBootRestAPI.dto.ResetPasswordRequest;import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.repository.UserRepository;import com.example.SpringBootRestAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
public class GuestInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get guest data
    @GetMapping("/profile_details")
    public ResponseEntity<?> getGuestData(Authentication auth) {
        try {
            // Get the auth user's username from the auth principal
            String username = auth.getName();
            // Fetch the current user by verifying username
            User user = userService.findByUsername(username);

            GuestDataRequest guestDataRequest = new GuestDataRequest(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getGender(),
                    user.getEmail(),
                    user.getPhoneNumber());
            return ResponseEntity.ok(guestDataRequest);  // Returns the obj
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get guest data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update profile details
    @PatchMapping("/profile_details/edit")
    public ResponseEntity<String> editProfileDetails(@RequestBody GuestDataRequest guestDataRequest, Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.findByUsername(username);

            // Update user data
            user.setFirstName(guestDataRequest.getFirstName());
            user.setLastName(guestDataRequest.getLastName());
            user.setGender(guestDataRequest.getGender());

            // Save the updated user profile details
            userService.saveAndFlush(user);

            return new ResponseEntity<>("Profile details updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update profile details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Reset password
    @PatchMapping("/settings/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request, Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.findByUsername(username);

            // Verify old password
            if (!passwordEncoder.matches(request.getCurrentPasswd(), user.getPassword()))
                return new ResponseEntity<>("Current password is incorrect", HttpStatus.BAD_REQUEST);

            // Hash and update to new password
            user.setPassword(passwordEncoder.encode(request.getNewPasswd()));

            // Save updated user
            userService.save(user);

            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to reset password: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
