package com.example.Food_Delivery.DTO.Orders;

import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Model.DeliveryAddress;
import com.example.Food_Delivery.Model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResById {
    private String shopId;
    private String shopName;
    private DeliveryAddress deliveryAddress;
    private String orderId;
    private String foodName;
    private OrderStatus orderStatus;
    private Double price;
    private Double discountPrice;
}
