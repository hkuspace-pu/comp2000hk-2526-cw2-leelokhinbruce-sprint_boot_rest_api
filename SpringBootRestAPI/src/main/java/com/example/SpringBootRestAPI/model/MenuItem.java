package com.example.SpringBootRestAPI.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-generating number incrementally
    private int id;

    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;
}
