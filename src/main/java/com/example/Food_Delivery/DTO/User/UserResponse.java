package com.example.Food_Delivery.DTO.User;

import com.example.Food_Delivery.Model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String mob_num;
    private String gender;
    private UserRole Role;
}
