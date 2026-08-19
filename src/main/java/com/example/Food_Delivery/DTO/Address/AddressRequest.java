package com.example.Food_Delivery.DTO.Address;

import com.example.Food_Delivery.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String fullName;
    private String mobileNumber;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String pinCode;
    private boolean isDefaultAddress;
}
