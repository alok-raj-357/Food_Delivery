package com.example.Food_Delivery.DTO.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String cartItemId;
    private String foodId;
    private Integer qty;
}
