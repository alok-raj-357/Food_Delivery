package com.example.Food_Delivery.DTO.Food;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponse {
    private String foodId;
    private String foodName;
    private Double price;
    private Float discount;
    private boolean isActive;
    private String shopName;
}
