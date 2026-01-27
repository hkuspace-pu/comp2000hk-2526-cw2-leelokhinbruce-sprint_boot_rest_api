package com.example.SpringBootRestAPI.model;

import jakarta.persistence.*;
import lombok.Data;import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table (name = "staff")
public class Staff {
    // Reference to the id from User as both PK and FK
    @Id
    private String id;

    private String position, workingBranch;

    @OneToOne  // Staff references User
    @MapsId  // Share the id value with User
    @JoinColumn(name = "id")  // Reference User's id
    private User user;

    // Constructor
    public Staff() {}

    public Staff(String position, String workingBranch, User user) {
        this.position = position;
        this.workingBranch = workingBranch;
        this.user = user;
        this.id = user.getId();  // Sync id
    }
}
