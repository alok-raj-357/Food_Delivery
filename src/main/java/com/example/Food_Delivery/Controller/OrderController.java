package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.Model.OrderStatus;
import com.example.Food_Delivery.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam String addressId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.placeOrder(email, addressId)
        );
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getOrders(email)
        );
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<?> getOrder(
            @PathVariable String orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getOrder(email, orderId)
        );
    }
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable String orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.cancelOrder(email, orderId)
        );
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.updateOrderStatus(email, orderId, status)
        );
    }
}
