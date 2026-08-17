package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Orders;
import com.example.Food_Delivery.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
    Optional<Payment> findByOrders(Orders orders);
}
