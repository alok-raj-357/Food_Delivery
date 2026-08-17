package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.Model.Food;
import com.example.Food_Delivery.Model.Review;
import com.example.Food_Delivery.Model.ShopStatus;
import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Repository.FoodRepository;
import com.example.Food_Delivery.Repository.ReviewRepository;
import com.example.Food_Delivery.Repository.UserRepository;
import jakarta.validation.constraints.Digits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public String addReview(
            String email,
            String foodId,
            Review review) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() ->
                        new RuntimeException("Food not found"));

        if (!food.isActive()) {
            throw new RuntimeException("Food is not available");
        }

        if (food.getShop() == null ||
                food.getShop().getShopStatus() != ShopStatus.ACTIVE) {
            throw new RuntimeException("Shop is not active");
        }

        if (review.getRating() == null ||
                review.getRating() < 1 ||
                review.getRating() > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        if (reviewRepository.findByUserAndFood(user, food).isPresent()) {
            throw new RuntimeException(
                    "You have already reviewed this food"
            );
        }

        review.setUser(user);
        review.setFood(food);

        reviewRepository.save(review);

        return "Review added successfully";
    }

    public List<Review> getFoodReviews(String foodId) {

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() ->
                        new RuntimeException("Food not found"));

        return reviewRepository.findByFood(food);
    }

    public String updateReview(
            String email,
            String reviewId,
            Review review) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Review oldReview = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!oldReview.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Review does not belong to user"
            );
        }

        if (review.getRating() == null ||
                review.getRating() < 1 ||
                review.getRating() > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());

        reviewRepository.save(oldReview);

        return "Review updated successfully";
    }

    public String deleteReview(
            String email,
            String reviewId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if (!review.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Review does not belong to user"
            );
        }

        reviewRepository.delete(review);

        return "Review deleted successfully";
    }
}
