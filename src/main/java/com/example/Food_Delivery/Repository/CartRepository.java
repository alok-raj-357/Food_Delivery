package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Cart;
import com.example.Food_Delivery.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
