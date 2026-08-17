package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser(User user);
}
