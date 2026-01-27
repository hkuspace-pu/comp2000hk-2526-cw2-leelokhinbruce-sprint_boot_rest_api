package com.example.SpringBootRestAPI.dto;

import lombok.Data;@Data
public class ResetPasswordRequest {
    private String currentPasswd, newPasswd;
}
