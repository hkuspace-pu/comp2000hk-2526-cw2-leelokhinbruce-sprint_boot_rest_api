package com.example.SpringBootRestAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // auto-generating UUID compliant with RFC 4122
    private String id;

    private String username, phoneNumber, firstName, lastName, gender, email;
    private String password;  // Hash with BCrypt

    // many users to one role
    @ManyToOne
    private Role role;
}
