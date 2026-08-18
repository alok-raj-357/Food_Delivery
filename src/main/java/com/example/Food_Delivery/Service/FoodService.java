package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Food.FoodRequest;
import com.example.Food_Delivery.DTO.Food.FoodResponse;
import com.example.Food_Delivery.DTO.Food.FoodUpdate;
import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.Model.Food;
import com.example.Food_Delivery.Model.Shop;
import com.example.Food_Delivery.Repository.FoodRepository;
import com.example.Food_Delivery.Repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final ShopRepository shopRepository;

    public FoodResponse createFood(FoodRequest foodRequest) {
        if (foodRepository.existsByFoodName(foodRequest.getFoodName()))
           throw new RuntimeException("Food Already Exists");

        Shop shop = shopRepository.findByOwnerEmail(foodRequest.getOwnerEmail())
                .orElseThrow(()->new RuntimeException("This ownerEmail is incorrect"));

        Food food = Food.builder()
                .shop(shop)
                .foodName(foodRequest.getFoodName())
                .price(foodRequest.getPrice())
                .discount(foodRequest.getDiscount())
                .build();
        Food savedFood = foodRepository.save(food);
        return mapToResponse(savedFood);
    }

    public FoodResponse mapToResponse(Food savedFood){
        FoodResponse foodResponse = FoodResponse.builder()
                .foodId(savedFood.getFoodId())
                .foodName(savedFood.getFoodName())
                .price(savedFood.getPrice())
                .discount(savedFood.getDiscount())
                .isActive(savedFood.isActive())
                .shopName(savedFood.getShop().getShopName())
                .build();
        return foodResponse;
    }

    public FoodResponse seeFood(String foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new RuntimeException("FoodId Not Found"));
        return mapToResponse(food);
    }

    public List<FoodResponse> seeAllFood() {
        List<Food> foods = foodRepository.findAll();
        return foods.stream().map(this::mapToResponse).toList();
    }

    public  FoodResponse updateFood(FoodUpdate foodUpdate) {
        Food food = foodRepository.findById(foodUpdate.getFoodId())
                .orElseThrow(()-> new RuntimeException(" "));

        if(foodUpdate.getFoodName()!=null) {
            if (foodRepository.existsByFoodName(foodUpdate.getFoodName()))
                throw new RuntimeException("Food Already Exists");
            food.setFoodName(foodUpdate.getFoodName());
        }
        if(foodUpdate.getPrice()!=null) food.setPrice(foodUpdate.getPrice());
        if(foodUpdate.getDiscount()!=null) food.setDiscount(foodUpdate.getDiscount());
        Food savedFood = foodRepository.save(food);
        return mapToResponse(savedFood);
    }

    public Message changeActiveStatus(String foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(()-> new RuntimeException("Invalid foodId"));
        if (!food.isActive()){
            food.setActive(true);
            foodRepository.save(food);
            return new Message("Food get Activated");
        }
        food.setActive(false);
        foodRepository.save(food);
        return new Message("Food get inActivated");
    }
}
