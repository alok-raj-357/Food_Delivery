package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Address.AddressRequest;
import com.example.Food_Delivery.DTO.Address.AddressResponse;
import com.example.Food_Delivery.Model.Address;
import com.example.Food_Delivery.Model.User;
import com.example.Food_Delivery.Repository.AddressRepository;
import com.example.Food_Delivery.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponse addAddress(String email, AddressRequest addressRequest) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }


        Address address = Address.builder()
                .fullName(addressRequest.getFullName())
                .mobileNumber(addressRequest.getMobileNumber())
                .houseNumber(addressRequest.getHouseNumber())
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .pinCode(addressRequest.getPinCode())
                .user(user)
                .build();

        if (addressRequest.isDefaultAddress()) {

            List<Address> addresses =
                    addressRepository.findByUser(user);

            for (Address oldAddress : addresses) {
                oldAddress.setDefaultAddress(false);
            }
            address.setDefaultAddress(true);
            addressRepository.saveAll(addresses);

        } else {

            List<Address> addresses =
                    addressRepository.findByUser(user);

            if (addresses.isEmpty()) {
                address.setDefaultAddress(true);
            }
        }


        Address savedAddress = addressRepository.save(address);
        return maptoResponse(savedAddress);
    }
    public AddressResponse maptoResponse(Address savedAddress){
        AddressResponse response = AddressResponse.builder()
                .addressId(savedAddress.getAddressId())
                .fullName(savedAddress.getFullName())
                .mobileNumber(savedAddress.getMobileNumber())
                .houseNumber(savedAddress.getHouseNumber())
                .street(savedAddress.getStreet())
                .city(savedAddress.getCity())
                .state(savedAddress.getState())
                .pinCode(savedAddress.getPinCode())
                .build();
        return response;
    }

    public List<AddressResponse> getAddresses(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        List<Address> addresses =  addressRepository.findByUser(user);
        return addresses.stream().map(this::maptoResponse).toList();
    }

    public AddressResponse updateAddress(String email, String addressId, AddressRequest addressRequest) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        Address oldAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!oldAddress.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new RuntimeException("Address does not belong to user");
        }

        oldAddress.setFullName(addressRequest.getFullName());
        oldAddress.setMobileNumber(addressRequest.getMobileNumber());
        oldAddress.setHouseNumber(addressRequest.getHouseNumber());
        oldAddress.setStreet(addressRequest.getStreet());
        oldAddress.setCity(addressRequest.getCity());
        oldAddress.setState(addressRequest.getState());
        oldAddress.setPinCode(addressRequest.getPinCode());

        if (addressRequest.isDefaultAddress()) {
            List<Address> addresses = addressRepository.findByUser(user);
            for (Address a : addresses) {
                a.setDefaultAddress(false);
            }
            addressRepository.saveAll(addresses);
            oldAddress.setDefaultAddress(true);
        }
        addressRepository.save(oldAddress);
        return maptoResponse(oldAddress);
    }

    public String deleteAddress(String email, String addressId) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId()
                .equals(user.getUserId())) {
            throw new RuntimeException("Address does not belong to user");
        }
        addressRepository.delete(address);
        return "Address deleted successfully";
    }
}
