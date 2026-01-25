package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.dto.GuestDataRequest;
import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest")
public class GuestInfoController {
    @Autowired
    private UserService userService;

    @GetMapping("/guest_info")
    public ResponseEntity<GuestDataRequest> getGuestData(Authentication auth) {
        try {
            String username = auth.getName();
            User user = userService.findByUsername(username);

            GuestDataRequest guestDataRequest = new GuestDataRequest(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getGender(),
                    user.getEmail(),
                    user.getPhoneNumber());
            return ResponseEntity.ok(guestDataRequest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
