package com.example.SpringBootRestAPI.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail, password;
}
