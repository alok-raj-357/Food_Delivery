package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(
            @RequestBody Address address,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.addAddress(email, address)
        );
    }

    @GetMapping("/getAddress")
    public ResponseEntity<?> getAddresses(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.getAddresses(email)
        );
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable String addressId,
            @RequestBody Address address,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.updateAddress(email, addressId, address)
        );
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable String addressId,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                addressService.deleteAddress(email, addressId)
        );
    }
}
