package com.example.SpringBootRestAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MenuItemRequest {
    private String foodName, category, mealTime;
    private double price;

    @JsonProperty("isAvailable")
    private boolean isAvailable;
    @JsonProperty("isPromotion")
    private boolean isPromotion;
}
