package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.DTO.User.UserRequest;
import com.example.Food_Delivery.DTO.User.UserResponse;
import com.example.Food_Delivery.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/findUser")
    public ResponseEntity<UserResponse> findUser(Authentication authentication){
        return ResponseEntity.ok(userService.findUser(authentication.getName()));
    }

    @PatchMapping("/updateUser")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest,Authentication authentication){
        return ResponseEntity.ok(userService.updateUser(userRequest,authentication.getName()));
    }

    @DeleteMapping("/InActivateUser")
    public ResponseEntity<Message> deleteUser(Authentication authentication){
        return ResponseEntity.ok(userService.deleteUser(authentication.getName()));
    }

    @PatchMapping("/ActivateUser/{email}")
    public ResponseEntity<Message> ActivateUser(@PathVariable String email){
        return ResponseEntity.ok(userService.ActivateUser(email));
    }
}
