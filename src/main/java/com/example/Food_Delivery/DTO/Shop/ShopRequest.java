package com.example.Food_Delivery.DTO.Shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRequest {
    private String shopName;
    private String ownerName;
    private String ownerEmail;
    private String description;
}
