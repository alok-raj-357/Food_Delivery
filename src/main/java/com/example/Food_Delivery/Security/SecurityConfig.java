package com.example.Food_Delivery.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private  JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests->
                        authorizeRequests
                                .requestMatchers("/api/auth/Register","/api/auth/Login","/api/restaurant/ReadShop/**","/api/Food/**","/api/review/food/**","/api/restaurant/createShop",
                                        "/api/restaurant/UpdateShop","/api/restaurant/DeleteShop/**","/api/restaurant/ReActivateOwner/**","/api/order/cancel/**").permitAll()

                                .requestMatchers("/api/user/findUser","/api/user/updateUser","/api/user/InActivateUser","/api/cart/**",
                                        "/api/address/**","/api/order/place","/api/order/getOrders","/api/payment/pay/**","/api/review/add/**","/api/review/update/**","/api/review/delete/**").hasRole("USER")

                                .requestMatchers("/api/auth/create").hasRole("SUPER_ADMIN")

                                .requestMatchers("/api/order/status/**","/api/user/ActivateUser/**","/api/payment/getPayment/**").hasAnyRole("ADMIN","SUPER_ADMIN")

                                .requestMatchers("/api/order/getOrder/**").hasAnyRole("USER","ADMIN")

                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
