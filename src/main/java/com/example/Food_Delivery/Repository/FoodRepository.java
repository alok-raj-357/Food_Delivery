package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,String> {
    boolean existsByFoodName(String foodName);
    Optional<Food> findByFoodId(String foodId);
}
