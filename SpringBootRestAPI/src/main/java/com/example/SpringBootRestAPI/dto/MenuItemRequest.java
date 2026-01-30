package com.example.SpringBootRestAPI.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MenuItemRequest {
    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;
}
