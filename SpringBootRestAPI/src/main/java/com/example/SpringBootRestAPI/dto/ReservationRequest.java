package com.example.SpringBootRestAPI.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    // reservation details
    private String date, time, status, occasion, specialOffer, reason;
    private String bookingNo;
    private int partySize;

    // guest details for reference
    private String name, email, phoneNumber;
}
