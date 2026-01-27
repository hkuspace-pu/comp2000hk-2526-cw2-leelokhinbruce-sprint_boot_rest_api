package com.example.SpringBootRestAPI.dto;

import lombok.Data;

@Data
public class StaffDataRequest {
    private String username, email, phoneNumber, workingBranch, position;

    public StaffDataRequest(String username, String email, String phoneNumber, String workingBranch, String position) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.workingBranch = workingBranch;
        this.position = position;
    }
}
