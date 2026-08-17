package com.example.Food_Delivery.DTO.Shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {
    private String shopId;
    private String ShopName;
    private String ownerName;
    private String ownerEmail;
    private String description;
    private String rating;
}
