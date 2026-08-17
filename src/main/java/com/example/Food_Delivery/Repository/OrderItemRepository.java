package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,String> {
}
