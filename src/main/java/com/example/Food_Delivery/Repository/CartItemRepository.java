package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Cart;
import com.example.Food_Delivery.Model.CartItem;
import com.example.Food_Delivery.Model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    Optional<CartItem> findByCartAndFood(Cart cart, Food food);

    List<CartItem> findByCart(Cart cart);
}
