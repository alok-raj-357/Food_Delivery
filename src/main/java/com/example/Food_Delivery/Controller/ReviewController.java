package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Review.ReviewRequest;
import com.example.Food_Delivery.DTO.Review.ReviewResponse;
import com.example.Food_Delivery.DTO.Review.UpdateReviewRequest;
import com.example.Food_Delivery.Model.Review;
import com.example.Food_Delivery.Service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody ReviewRequest reviewRequest,
            Authentication authentication) {
        return ResponseEntity.ok(reviewService.addReview(authentication.getName(),  reviewRequest));
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<List<ReviewResponse>> getFoodReviews(
            @PathVariable String foodId) {
        return ResponseEntity.ok(reviewService.getFoodReviews(foodId));
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable String reviewId,
            @RequestBody UpdateReviewRequest updateReviewRequest,
            Authentication authentication) {
        return ResponseEntity.ok(reviewService.updateReview(authentication.getName(), reviewId, updateReviewRequest));
    }
}
