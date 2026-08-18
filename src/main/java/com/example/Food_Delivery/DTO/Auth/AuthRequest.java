package com.example.Food_Delivery.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String mob_num;
    private String gender;
}
