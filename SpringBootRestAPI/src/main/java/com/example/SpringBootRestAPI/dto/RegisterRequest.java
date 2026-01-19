package com.example.SpringBootRestAPI.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username, email, password, firstName, lastName, phoneNumber, gender;
}
