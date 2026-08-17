package com.example.Food_Delivery.Config;

import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Model.UserRole;
import com.example.Food_Delivery.Model.UserStatus;
import com.example.Food_Delivery.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig {

    @Bean
    public CommandLineRunner createSuperAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String email = "alok30301@gmail.com";

            if (!userRepository.existsByEmail(email)) {

                User superAdmin = User.builder()
                        .firstName("Alok")
                        .lastName("Raj")
                        .email(email)
                        .password(
                                passwordEncoder.encode("AlokRaj@123")
                        )
                        .mobile_number("9534406178")
                        .gender("MALE")
                        .userStatus(UserStatus.ACTIVE)
                        .role(UserRole.SUPER_ADMIN)
                        .build();

                userRepository.save(superAdmin);
            }
        };
    }
}
