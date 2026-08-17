package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.Model.Review;
import com.example.Food_Delivery.Service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add/{foodId}")
    public ResponseEntity<?> addReview(
            @PathVariable String foodId,
            @RequestBody Review review,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.addReview(email, foodId, review)
        );
    }

    @GetMapping("/food/{foodId}")
    public ResponseEntity<?> getFoodReviews(
            @PathVariable String foodId) {

        return ResponseEntity.ok(
                reviewService.getFoodReviews(foodId)
        );
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable String reviewId,
            @RequestBody Review review,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.updateReview(email, reviewId, review)
        );
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable String reviewId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.deleteReview(email, reviewId)
        );
    }
}
