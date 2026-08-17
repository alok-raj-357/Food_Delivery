package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.DTO.User.UserRequest;
import com.example.Food_Delivery.DTO.User.UserResponse;
import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Model.UserStatus;
import com.example.Food_Delivery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserResponse findUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("User Doesn't Exist");
        }
        return authService.mapToResponse(user);
    }

    public UserResponse updateUser(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null){
            throw new RuntimeException("User Email Not Found");
        }
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setMobile_number(userRequest.getMob_num());
        user.setGender(userRequest.getGender());
        User savedUser = userRepository.save(user);
        return authService.mapToResponse(savedUser);
    }


    public Message deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("This Email doesn't Exist");
        }
        if(user.getUserStatus() == UserStatus.INACTIVE){    // if user is deleted
            throw new RuntimeException("User Status is Already InActive");
        }
        if (user.getUserStatus() == UserStatus.BLOCKED){
            throw new RuntimeException("User is Blocked By Admin So User can't Delete their Account");
        }
        if (user.getUserStatus() == UserStatus.ACTIVE){
            user.setUserStatus(UserStatus.INACTIVE);
            userRepository.save(user);
        }
        return new Message("Your Account is succesfully Deleted");
    }

    public Message ActivateUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("User Email Not Found");
        }
        if (user.getUserStatus() == UserStatus.ACTIVE){
            throw new RuntimeException("User Already Active");
        }
        if (user.getUserStatus() == UserStatus.BLOCKED){
            throw new RuntimeException("User is Blocked. So User can't Activate their account");
        }
        if (user.getUserStatus() == UserStatus.INACTIVE){
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return new Message("User get Activated");
    }
}
