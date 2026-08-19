package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Orders.OrderResById;
import com.example.Food_Delivery.DTO.Orders.OrdersResponse;
import com.example.Food_Delivery.Model.*;
import com.example.Food_Delivery.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
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

    private DeliveryAddress mapToDeliveryAddress(Address address) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();

        deliveryAddress.setFullName(address.getFullName());
        deliveryAddress.setMobileNo(address.getMobileNumber());
        deliveryAddress.setStreetAddress(address.getStreet());
        deliveryAddress.setCity(address.getCity());
        deliveryAddress.setState(address.getState());
        deliveryAddress.setPinCode(address.getPinCode());

        return deliveryAddress;
    }

    public String placeOrder(String email, String addressId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cart.getCartItems();

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
                .totalAmount(0.0)
                .deliveryAddress(mapToDeliveryAddress(address))
                .orderStatus(OrderStatus.PENDING)
                .build();



        double totalAmount = 0;
        double totalPrice = 0;
        List<OrderItem>orderItems  = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Food food = cartItem.getFood();

            if (!food.isActive()) {
                throw new RuntimeException(food.getFoodName() + " is not available");
            }
            if (food.getShop() == null ||
                    food.getShop().getShopStatus() != ShopStatus.ACTIVE) {
                throw new RuntimeException(food.getFoodName() + "'s shop is not active");
            }

            double price = food.getPrice();
            double toPrice = price * cartItem.getQuantity();

            if (food.getDiscount() != null && food.getDiscount() > 0) {
                price = price - (price * food.getDiscount() / 100);
            }

            double itemTotal = price * cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .orders(orders)
                    .food(food)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getQuantity()*price)
                    .build();
            orderItems.add(orderItem);
            totalAmount += itemTotal;
            totalPrice += toPrice;

        }

        orders.setTotalAmount(totalAmount);
        orders.setOrderItems(orderItems);
        orders.setTotalPrice(totalPrice);
        ordersRepository.save(orders);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return "Order is Pending";
    }

    public List<OrdersResponse> getOrders(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        List<Orders> orders =  ordersRepository.findByUser(user);
        return orders.stream().map(this::maptoOrderResponse).toList();

    }

    private OrdersResponse maptoOrderResponse(Orders orders) {
        OrdersResponse response = new OrdersResponse();

        List<String> names = new ArrayList<>() ;
        for(OrderItem orderItem:orders.getOrderItems()){
            names.add(orderItem.getFood().getFoodName());
        }
        List<OrderItem> orderItems = orders.getOrderItems();
        if(orderItems.isEmpty()) throw new RuntimeException("OrderItems are Empty");

        response.setShopName(orderItems.getFirst().getFood().getShop().getShopName());
        response.setOrderItemName(names);
        response.setPrice(orders.getTotalAmount());
        response.setOrderStatus(orders.getOrderStatus());
        response.setOrderConfirmedAt(orders.getOrderConfirmedAt());
        return response;
    }


    public OrderResById getOrder(String email, String orderId) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!orders.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Order does not belong to user");
        }

        return maptoOrderResById(orders);
    }

    private OrderResById maptoOrderResById(Orders orders){
        OrderResById orderResById = new OrderResById();
        Food food = orders.getOrderItems().getFirst().getFood();

        orderResById.setShopId(food.getShop().getShopId());
        orderResById.setShopName(food.getShop().getShopName());
        orderResById.setDeliveryAddress(orders.getDeliveryAddress());
        orderResById.setOrderId(orders.getOrderId());
        orderResById.setFoodName(food.getFoodName());
        orderResById.setOrderStatus(orders.getOrderStatus());
        orderResById.setPrice(orders.getTotalPrice());
        orderResById.setDiscountPrice(orders.getTotalAmount());

        return orderResById;
    }


    public String cancelOrder(String orderId) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orders.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered order cannot be cancelled");
        }

        if (orders.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }
        orders.setOrderStatus(OrderStatus.CANCELLED);
        orders.setCancelledAt(LocalDateTime.now());

        ordersRepository.save(orders);

        return "Order cancelled successfully";
    }

    public String updateOrderStatus(String email, String orderId, OrderStatus status) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        if (user.getRole() != UserRole.ADMIN && user.getRole()!=UserRole.SUPER_ADMIN) {
            throw new RuntimeException("Only admin can update order status");
        }
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orders.getDeliveredAt()!=null) {
            throw new RuntimeException("Order is already delivered");
        }

        if (orders.getCancelledAt()!=null) {
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

            case PREPARING:
                if(orders.getOrderConfirmedAt()==null) throw new RuntimeException("Orders Not Confirmed yet");
                if(orders.getOutForDeliveryAt()!=null) throw new RuntimeException("Orders is in out for delivery");
                if (orders.getPreparingAt() == null) {
                    orders.setPreparingAt(LocalDateTime.now());
                }
                break;

            case OUT_FOR_DELIVERY:
                if(orders.getPreparingAt()==null) throw new RuntimeException("Orders is not Prepared yet");
                if (orders.getOutForDeliveryAt() == null) {
                    orders.setOutForDeliveryAt(LocalDateTime.now());
                }
                break;

            case DELIVERED:
                if(orders.getOutForDeliveryAt()==null) throw new RuntimeException("Orders is not Out For Delivery");
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
