package com.example.Food_Delivery.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String foodId;

    private String foodName;
    private Double price;
    private Float discount;

    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "shop_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_food_shop")
    )
    private Shop shop;

    @OneToMany(mappedBy = "food")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "food")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "food")
    private List<Review> reviews = new ArrayList<>();
}
