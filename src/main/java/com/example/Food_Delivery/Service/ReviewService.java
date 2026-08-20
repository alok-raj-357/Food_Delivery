package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Review.ReviewRequest;
import com.example.Food_Delivery.DTO.Review.ReviewResponse;
import com.example.Food_Delivery.DTO.Review.UpdateReviewRequest;
import com.example.Food_Delivery.Model.*;
import com.example.Food_Delivery.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final FoodRepository foodRepository;

    public ReviewResponse addReview(String email, ReviewRequest reviewRequest) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        OrderItem orderItem = orderItemRepository.findByOrderItemIdAndOrdersUserUserId(reviewRequest.getOrderItemId(), user.getUserId())
                .orElseThrow (()-> new RuntimeException("OrderItems not Found"));

        if(orderItem.getOrders().getDeliveredAt()==null)
            throw new RuntimeException("Only delivered food can be reviewed");

        if (reviewRepository.existsByUserAndOrderItem(user, orderItem)) {
            throw new RuntimeException("You have already reviewed this food");
        }

        Food food = orderItem.getFood();
        if (food == null){
            throw new RuntimeException("Food Not Found");
        }

        if (!food.isActive()) {
            throw new RuntimeException("Food is not available");
        }

        if (food.getShop() == null ||
                food.getShop().getShopStatus() != ShopStatus.ACTIVE) {
            throw new RuntimeException("Shop is not active");
        }

        if (reviewRequest.getRating() == null ||
                reviewRequest.getRating() < 1 ||
                reviewRequest.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setUser(user);
        review.setFood(food);
        review.setOrderItem(orderItem);

        Review savedReview  = reviewRepository.save(review);
        return mapToReviewResponse(savedReview);
    }

    private ReviewResponse mapToReviewResponse(Review savedReview) {
        ReviewResponse response = new ReviewResponse();

        response.setReviewId(savedReview.getReviewId());
        response.setOrderItemId(savedReview.getOrderItem().getOrderItemId());
        response.setUserName(savedReview.getUser().getFirstName()+" "+savedReview.getUser().getLastName());
        response.setFoodId(savedReview.getFood().getFoodId());
        response.setComment(savedReview.getComment());
        response.setRating(savedReview.getRating());
        return response;
    }

    public List<ReviewResponse> getFoodReviews(String foodId) {

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        List<Review> reviews=reviewRepository.findByFood(food);
        return reviews.stream().map(this::mapToReviewResponse).toList();

    }

    public ReviewResponse updateReview(
            String email,
            String reviewId,
            UpdateReviewRequest updateReviewRequest) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Review oldReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!oldReview.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException("Review does not belong to user");
        }

        if (updateReviewRequest.getRating() == null ||
                updateReviewRequest.getRating() < 1 ||
                updateReviewRequest.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        oldReview.setRating(updateReviewRequest.getRating());
        oldReview.setComment(updateReviewRequest.getComment());

        Review saveReview = reviewRepository.save(oldReview);

        return mapToReviewResponse(saveReview);
    }
}
