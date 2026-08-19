package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Orders.OrderResById;
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
    public ResponseEntity<String> placeOrder(
            @RequestParam String addressId,
            Authentication authentication) {
        return ResponseEntity.ok(orderService.placeOrder(authentication.getName(), addressId));
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrders(authentication.getName()));
    }

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<OrderResById> getOrder(
            @PathVariable String orderId,
            Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrder(authentication.getName(), orderId));
    }
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @PatchMapping("/status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {

        return ResponseEntity.ok(orderService.updateOrderStatus(authentication.getName(), orderId, status));
    }
}
