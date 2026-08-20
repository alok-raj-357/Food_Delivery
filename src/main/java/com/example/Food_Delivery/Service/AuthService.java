package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Auth.AuthRequest;
import com.example.Food_Delivery.DTO.User.LoginRequest;
import com.example.Food_Delivery.DTO.User.LoginResponse;
import com.example.Food_Delivery.DTO.User.UserResponse;
import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Model.UserRole;
import com.example.Food_Delivery.Model.UserStatus;
import com.example.Food_Delivery.Repository.UserRepository;
import com.example.Food_Delivery.Security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserResponse register(AuthRequest authrequest){
        User user = User.builder()
                .email(authrequest.getEmail())
                .password(passwordEncoder.encode(authrequest.getPassword()))
                .firstName(authrequest.getFirstName())
                .lastName(authrequest.getLastName())
                .mobile_number(authrequest.getMob_num())
                .gender(authrequest.getGender())
                .build();
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }
    public UserResponse mapToResponse(User savedUser){
     UserResponse response = UserResponse.builder()
             .userId(savedUser.getUserId())
             .email(savedUser.getEmail())
             .firstName(savedUser.getFirstName())
             .lastName(savedUser.getLastName())
             .mob_num(savedUser.getMobile_number())
             .gender(savedUser.getGender())
             .Role(savedUser.getRole())
             .build();
     return response;
    }
    public LoginResponse authenticate(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) throw new RuntimeException("This Email Doesn't Exist");
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect Password");

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token);
    }

    public String createAdmin(String email, AuthRequest authRequest) {

        User superAdmin = userRepository.findByEmail(email);

        if (superAdmin == null) {
            throw new RuntimeException("User Not Found");
        }

        if (superAdmin.getRole() != UserRole.SUPER_ADMIN) {
            throw new RuntimeException(
                    "Only Super Admin can create Admin"
            );
        }

        if (userRepository.findByEmail(authRequest.getEmail()) != null) {
            throw new RuntimeException(
                    "Email already exists"
            );
        }
        User user = new User();
        user.setRole(UserRole.ADMIN);
        user.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        return "Admin created successfully";
    }
}

