package com.example.SpringBootRestAPI.dto;

import lombok.Data;

@Data
public class GuestDataRequest {
    private String firstName, lastName, gender, email, phoneNumber;

    public GuestDataRequest(String firstName, String lastName, String gender, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
