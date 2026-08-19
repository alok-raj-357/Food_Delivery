package com.example.Food_Delivery.DTO.Orders;

import com.example.Food_Delivery.Model.OrderItem;
import com.example.Food_Delivery.Model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    private String shopName;
    private List<String> orderItemName;
    private Double price;
    private OrderStatus orderStatus;
    private LocalDateTime orderConfirmedAt;
}
