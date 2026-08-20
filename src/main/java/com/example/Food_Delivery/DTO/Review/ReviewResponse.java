package com.example.Food_Delivery.DTO.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private String orderItemId;
    private String reviewId;
    private String foodId;
    private String userName;
    private Integer rating;
    private String comment;
}
