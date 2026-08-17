package com.example.Food_Delivery.Security;

import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Model.UserStatus;
import com.example.Food_Delivery.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Jwt filter called: ");
        String jwt = jwtUtils.getJwtFromHeader(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //  Invalid token → reject
        if (!jwtUtils.validateToken(jwt)) {
            throw new RuntimeException("Invalid or expired token");
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            String email = jwtUtils.getUserEmailFromJwt(jwt);


            User user = userRepository.findByEmail(email);

            if (user != null) {

                if (user.getUserStatus() != UserStatus.ACTIVE) {
                    throw new RuntimeException(
                            "User account is " + user.getUserStatus()
                    );
                }

            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
