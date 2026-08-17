package com.example.Food_Delivery.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String shopId;

    private String shopName;
    private String ownerName;

    @NotBlank(message = "Please Enter Email")
    @Column(unique = true)
    private String ownerEmail;

    private String description;
    private String rating;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ShopStatus shopStatus = ShopStatus.ACTIVE;

    @CreationTimestamp
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;

    @Builder.Default
    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Food> foods = new ArrayList<>();
}
