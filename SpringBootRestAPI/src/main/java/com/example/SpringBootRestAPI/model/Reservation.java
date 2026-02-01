package com.example.SpringBootRestAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;import jakarta.persistence.ManyToOne;import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Reservation {
    @Id
    private String id;

    // reservation details
    private String date, time, status, occasion, specialOffer, reason;
    private String bookingNo;
    private int partySize;

    // guest details for reference
    private String name, email, phoneNumber;

    @ManyToOne
    private User user;
}
