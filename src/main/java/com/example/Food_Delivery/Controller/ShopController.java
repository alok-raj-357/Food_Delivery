package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.DTO.Shop.ShopRequest;
import com.example.Food_Delivery.DTO.Shop.ShopResponse;
import com.example.Food_Delivery.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/createShop")
    public ResponseEntity<ShopResponse> createRestaurant(@RequestBody ShopRequest shopRequest){
        return new ResponseEntity<>(shopService.createRestaurant(shopRequest), HttpStatus.CREATED);
    }

    @GetMapping("/ReadShop/{ownerEmail}")
    public ResponseEntity<ShopResponse> readRestaurant(@PathVariable String ownerEmail){
        return ResponseEntity.ok(shopService.readRestaurant(ownerEmail));
    }

    @PutMapping("/UpdateShop")
    public ResponseEntity<ShopResponse> updateRestaurant( @RequestBody ShopRequest shopRequest){
        return ResponseEntity.ok(shopService.updateRestaurant(shopRequest));
    }

    @DeleteMapping("/DeleteShop/{ownerEmail}")
    public ResponseEntity<Message> deleteRestaurant(@PathVariable String ownerEmail){
        return ResponseEntity.ok(shopService.deleteRestaurant(ownerEmail));
    }

    @PatchMapping("/ReActivateOwner/{email}")
    public ResponseEntity<Message> ReActiveOwner(@PathVariable String email){
        return ResponseEntity.ok(shopService.ReActiveOwner(email));
    }
}
