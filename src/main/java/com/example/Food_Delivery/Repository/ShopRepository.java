package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,String> {
    Optional<Shop> findByOwnerEmail(String ownerEmail);
}
