package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Cart.CartResponse;
import com.example.Food_Delivery.Model.*;
import com.example.Food_Delivery.Repository.CartItemRepository;
import com.example.Food_Delivery.Repository.CartRepository;
import com.example.Food_Delivery.Repository.FoodRepository;
import com.example.Food_Delivery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    public String addToCart(String email, String foodId, Integer quantity) {


        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = userRepository.findByEmail(email);
        if(user==null) throw new RuntimeException("User Not Found");

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        if (!food.isActive()) {
            throw new RuntimeException("Food is not available");
        }

        if (food.getShop() == null ||
                food.getShop().getShopStatus() != ShopStatus.ACTIVE) {
            throw new RuntimeException("Shop is not active");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .build()
                ));

        Optional<CartItem> optionalCartItem =
                cartItemRepository.findByCartAndFood(cart, food);

        if (optionalCartItem.isPresent()) {

            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);

        } else {

            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .food(food)
                    .quantity(quantity)
                    .build();

            cartItemRepository.save(cartItem);
        }

        return "Food added to cart successfully";
    }

    public List<CartItem> getCart(String email) {

        User user = userRepository.findByEmail(email);
        if(user==null) throw new RuntimeException("User Not Found");

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        return cartItemRepository.findByCart(cart);
    }

    public String updateCart(
            String email,
            String cartItemId,
            Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        User user = userRepository.findByEmail(email);
        if(user==null) throw new RuntimeException("User Not Found");

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        if (!cartItem.getFood().isActive()) {
            throw new RuntimeException("Food is not available");
        }

        if (cartItem.getFood().getShop() == null ||
                cartItem.getFood().getShop().getShopStatus() != ShopStatus.ACTIVE) {
            throw new RuntimeException("Shop is not active");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return "Cart updated successfully";
    }

    public String removeCart(
            String email,
            String cartItemId) {

        User user = userRepository.findByEmail(email);
        if(user==null) throw new RuntimeException("User Not Found");

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartItemRepository.delete(cartItem);

        return "Food removed from cart successfully";
    }
}
