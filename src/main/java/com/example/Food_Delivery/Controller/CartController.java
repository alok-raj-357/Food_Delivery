package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam String foodId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                cartService.addToCart(email, foodId, quantity)
        );
    }

    @GetMapping("/get/cart")
    public ResponseEntity<?> getCart(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(cartService.getCart(email));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateCart(
            @PathVariable String cartItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        String email = authentication.getName();


        return ResponseEntity.ok(
                cartService.updateCart(email, cartItemId, quantity)
        );
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeCart(
            @PathVariable String cartItemId,
            Authentication authentication) {

        String email = authentication.getName();


        return ResponseEntity.ok(
                cartService.removeCart(email, cartItemId)
        );
    }
}
