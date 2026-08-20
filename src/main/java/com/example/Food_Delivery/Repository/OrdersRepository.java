package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Orders;
import com.example.Food_Delivery.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findByUser(User user);

}
