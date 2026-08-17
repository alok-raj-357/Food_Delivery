package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<?> makePayment(
            @PathVariable String orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                paymentService.makePayment(email, orderId)
        );
    }

    @GetMapping("/getPayment/{orderId}")
    public ResponseEntity<?> getPayment(
            @PathVariable String orderId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                paymentService.getPayment(email, orderId)
        );
    }
}