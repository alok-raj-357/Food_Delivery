package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.Model.*;
import com.example.Food_Delivery.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Transactional
    public String placeOrder(String email, String addressId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Address does not belong to user");
        }

        Orders orders = Orders.builder()
                .user(user)
                .address(address)
                .totalAmount(0.0)
                .orderStatus(OrderStatus.PENDING)
                .build();

        orders = ordersRepository.save(orders);

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {

            Food food = cartItem.getFood();

            if (!food.isActive()) {
                throw new RuntimeException(
                        food.getFoodName() + " is not available"
                );
            }

            if (food.getShop() == null ||
                    food.getShop().getShopStatus() != ShopStatus.ACTIVE) {
                throw new RuntimeException(
                        food.getFoodName() + "'s shop is not active"
                );
            }

            double price = food.getPrice();

            if (food.getDiscount() != null && food.getDiscount() > 0) {
                price = price - (price * food.getDiscount() / 100);
            }

            double itemTotal = price * cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .orders(orders)
                    .food(food)
                    .quantity(cartItem.getQuantity())
                    .price(price)
                    .build();

            orderItemRepository.save(orderItem);

            totalAmount += itemTotal;
        }

        orders.setTotalAmount(totalAmount);
        ordersRepository.save(orders);

        cartItemRepository.deleteAll(cartItems);

        return "Order placed successfully";
    }

    public List<Orders> getOrders(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        return ordersRepository.findByUser(user);
    }

    public Orders getOrder(String email, String orderId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!orders.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException(
                    "Order does not belong to user"
            );
        }

        return orders;
    }
    public String cancelOrder(String email, String orderId) {

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orders.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        if (orders.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }

        orders.setOrderStatus(OrderStatus.CANCELLED);

        ordersRepository.save(orders);

        return "Order cancelled successfully";
    }
    public String updateOrderStatus(
            String email,
            String orderId,
            OrderStatus status) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can update order status");
        }

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (orders.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order is already delivered");
        }

        if (orders.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is cancelled");
        }

        if (status == null) {
            throw new RuntimeException("Order status is required");
        }

        if (orders.getOrderStatus() == status) {
            throw new RuntimeException("Order is already in this status");
        }

        orders.setOrderStatus(status);

        switch (status) {

            case CONFIRMED:
                if (orders.getOrderConfirmedAt() == null) {
                    orders.setOrderConfirmedAt(LocalDateTime.now());
                }
                break;

            case PREPARING:
                if (orders.getPreparingAt() == null) {
                    orders.setPreparingAt(LocalDateTime.now());
                }
                break;

            case OUT_FOR_DELIVERY:
                if (orders.getOutForDeliveryAt() == null) {
                    orders.setOutForDeliveryAt(LocalDateTime.now());
                }
                break;

            case DELIVERED:
                if (orders.getDeliveredAt() == null) {
                    orders.setDeliveredAt(LocalDateTime.now());
                }
                break;

            case CANCELLED:
                if (orders.getCancelledAt() == null) {
                    orders.setCancelledAt(LocalDateTime.now());
                }
                break;

            default:
                break;
        }

        ordersRepository.save(orders);

        return "Order status updated successfully";
    }
}
