package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Address.AddressRequest;
import com.example.Food_Delivery.DTO.Address.AddressResponse;
import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<AddressResponse> addAddress(
            @RequestBody AddressRequest addressRequest,
            Authentication authentication) {

        return ResponseEntity.ok(addressService.addAddress(authentication.getName(), addressRequest));
    }

    @GetMapping("/getAddress")
    public ResponseEntity<List<AddressResponse>> getAddresses(Authentication authentication) {
        return ResponseEntity.ok(addressService.getAddresses(authentication.getName()));
    }

    @PutMapping("/update/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable String addressId,
            @RequestBody AddressRequest addressRequest,
            Authentication authentication) {

        return ResponseEntity.ok(addressService.updateAddress(authentication.getName(), addressId, addressRequest));
    }

    @DeleteMapping("/delete/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable String addressId,
            Authentication authentication) {
        return ResponseEntity.ok(addressService.deleteAddress(authentication.getName(), addressId));
    }
}
