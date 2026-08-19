package com.example.Food_Delivery.Repository;

import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser(User user);
}
