package com.example.SpringBootRestAPI.entity;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;import java.util.Set;

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

    @ManyToMany(targetEntity = Role.class)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
