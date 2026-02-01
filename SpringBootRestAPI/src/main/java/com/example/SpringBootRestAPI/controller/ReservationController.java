package com.example.SpringBootRestAPI.controller;

import com.example.SpringBootRestAPI.dto.ReservationRequest;
import com.example.SpringBootRestAPI.model.Reservation;
import com.example.SpringBootRestAPI.model.User;
import com.example.SpringBootRestAPI.service.ItemService;
import com.example.SpringBootRestAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('GUEST', 'STAFF')")
    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        try {
            return ResponseEntity.ok(itemService.findAllReservations());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get all reservations: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Make reservation
    @PreAuthorize("hasRole('GUEST')")
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request, Authentication auth) {
        try {
            // New a reservation
            // reservation details
            Reservation reservation = new Reservation();
            reservation.setDate(request.getDate());
            reservation.setTime(request.getTime());
            reservation.setStatus(request.getStatus());
            reservation.setOccasion(reservation.getOccasion());
            reservation.setSpecialOffer(reservation.getSpecialOffer());
            reservation.setReason(null);
            reservation.setBookingNo(reservation.getBookingNo());
            reservation.setPartySize(reservation.getPartySize());
            // guest details
            reservation.setName(reservation.getName());
            reservation.setEmail(reservation.getEmail());
            reservation.setPhoneNumber(reservation.getPhoneNumber());

            // Refer to a user account (marks which user make the reservation)
            String username = auth.getName();
            User user = userService.findByUsername(username);
            reservation.setUser(user);

            // Save reservation
            itemService.saveReservation(reservation);
            return ResponseEntity.ok(reservation);  // Return the reservation
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add reservation: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
