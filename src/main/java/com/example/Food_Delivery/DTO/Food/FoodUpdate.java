package com.example.Food_Delivery.DTO.Food;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodUpdate {
    @NotBlank(message = "Enter foodName")
    private String foodId;
    private String foodName;
    private Double price;
    private Float discount;
}
