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

    @Column(name = "food_name")
    private String foodName;
    private String category;
    @Column(name = "meal_time")
    private String mealTime;

    private double price;

    @Column(name = "is_available")
    private boolean isAvailable;
    @Column(name = "is_promotion")
    private boolean isPromotion;

    @Column(name = "updated_at")
    private Date updatedAt;
}
