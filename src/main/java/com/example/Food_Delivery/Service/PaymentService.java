package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.Model.*;
import com.example.Food_Delivery.Repository.OrdersRepository;
import com.example.Food_Delivery.Repository.PaymentRepository;
import com.example.Food_Delivery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    public String makePayment(String email, String orderId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!orders.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Order does not belong to user"
            );
        }

        if (orders.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(
                    "Payment cannot be done for this order"
            );
        }

        if (paymentRepository.findByOrders(orders).isPresent()) {
            throw new RuntimeException(
                    "Payment already exists"
            );
        }

        Payment payment = Payment.builder()
                .orders(orders)
                .amount(orders.getTotalAmount())
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId(UUID.randomUUID().toString())
                .build();

        paymentRepository.save(payment);

        orders.setOrderStatus(OrderStatus.CONFIRMED);
        orders.setOrderConfirmedAt(LocalDateTime.now());
        ordersRepository.save(orders);

        return "Payment successful";
    }

    public Payment getPayment(
            String email,
            String orderId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!orders.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Order does not belong to user"
            );
        }

        return paymentRepository.findByOrders(orders)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
