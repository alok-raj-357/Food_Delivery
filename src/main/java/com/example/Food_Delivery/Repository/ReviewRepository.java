package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Food;
import com.example.Food_Delivery.Model.Review;
import com.example.Food_Delivery.Model.User;
import jakarta.validation.constraints.Digits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Object> findByUserAndFood(User user, Food food);

    List<Review> findByFood(Food food);
}
