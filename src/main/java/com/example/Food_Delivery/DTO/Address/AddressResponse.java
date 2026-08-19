package com.example.Food_Delivery.DTO.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private String addressId;
    private String fullName;
    private String mobileNumber;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String pinCode;
}
