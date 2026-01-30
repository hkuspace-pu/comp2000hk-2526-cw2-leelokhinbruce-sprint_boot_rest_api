package com.example.SpringBootRestAPI.model;

import jakarta.persistence.*;
import lombok.Data;import java.util.Date;

@Data
@Entity
@Table
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-generating number incrementally
    private int id;  // server id

    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;

    private Date updatedAt;
}
