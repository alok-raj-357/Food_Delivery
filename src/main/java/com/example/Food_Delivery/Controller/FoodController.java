package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Food.FoodRequest;
import com.example.Food_Delivery.DTO.Food.FoodResponse;
import com.example.Food_Delivery.DTO.Food.FoodUpdate;
import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.Service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/Addfood")
    public ResponseEntity<FoodResponse> createFood(@Valid @RequestBody FoodRequest foodRequest){
        return new ResponseEntity<>(foodService.createFood(foodRequest), HttpStatus.CREATED);
    }

    @GetMapping("/Findfood/{foodId}")
    public ResponseEntity<FoodResponse> seeFood(@PathVariable String foodId){
        return ResponseEntity.ok(foodService.seeFood(foodId));
    }

    @GetMapping("/Findallfood")
    public ResponseEntity<List<FoodResponse>> seeFood(){
        return ResponseEntity.ok(foodService.seeAllFood());
    }

    @PatchMapping("/Updatefood")
    public ResponseEntity<FoodResponse> updateFood(@Valid @RequestBody FoodUpdate foodUpdate){
        return ResponseEntity.ok(foodService.updateFood(foodUpdate));
    }

    @DeleteMapping("/Change_ActiveStatus/{foodId}")
    public ResponseEntity<Message> changeActiveStatus(@PathVariable String foodId){
        return ResponseEntity.ok(foodService.changeActiveStatus(foodId));
    }
}
