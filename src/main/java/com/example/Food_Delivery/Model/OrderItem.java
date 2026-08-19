package com.example.Food_Delivery.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderItemId;

    private Integer quantity;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id",
            nullable = false
    )
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "food_id",
            nullable = false
    )
    private Food food;
}
