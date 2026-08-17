package com.example.Food_Delivery.Controller;

import com.example.Food_Delivery.DTO.User.LoginRequest;
import com.example.Food_Delivery.DTO.User.LoginResponse;
import com.example.Food_Delivery.DTO.User.UserRequest;
import com.example.Food_Delivery.DTO.User.UserResponse;
import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/Register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest){
        return new ResponseEntity<>(authService.register(userRequest), HttpStatus.CREATED);
    }
    @PostMapping("/Login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginrequest){
         return ResponseEntity.ok(authService.authenticate(loginrequest));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @RequestBody User user,
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                authService.createAdmin(email, user)
        );
    }
}
