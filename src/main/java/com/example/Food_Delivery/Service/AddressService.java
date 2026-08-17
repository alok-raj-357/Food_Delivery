package com.example.Food_Delivery.Service;

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

    public String addAddress(String email, Address address) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        address.setUser(user);

        if (address.isDefaultAddress()) {

            List<Address> addresses =
                    addressRepository.findByUser(user);

            for (Address oldAddress : addresses) {
                oldAddress.setDefaultAddress(false);
            }

            addressRepository.saveAll(addresses);

        } else {

            List<Address> addresses =
                    addressRepository.findByUser(user);

            if (addresses.isEmpty()) {
                address.setDefaultAddress(true);
            }
        }

        addressRepository.save(address);

        return "Address added successfully";
    }

    public List<Address> getAddresses(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        return addressRepository.findByUser(user);
    }

    public String updateAddress(
            String email,
            String addressId,
            Address address) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Address oldAddress = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        if (!oldAddress.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Address does not belong to user"
            );
        }

        oldAddress.setFullName(address.getFullName());
        oldAddress.setMobileNumber(address.getMobileNumber());
        oldAddress.setHouseNumber(address.getHouseNumber());
        oldAddress.setStreet(address.getStreet());
        oldAddress.setCity(address.getCity());
        oldAddress.setState(address.getState());
        oldAddress.setPinCode(address.getPinCode());

        if (address.isDefaultAddress()) {

            List<Address> addresses =
                    addressRepository.findByUser(user);

            for (Address a : addresses) {
                a.setDefaultAddress(false);
            }

            addressRepository.saveAll(addresses);

            oldAddress.setDefaultAddress(true);
        }

        addressRepository.save(oldAddress);

        return "Address updated successfully";
    }

    public String deleteAddress(
            String email,
            String addressId) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new RuntimeException("Address not found"));

        if (!address.getUser().getUserId()
                .equals(user.getUserId())) {

            throw new RuntimeException(
                    "Address does not belong to user"
            );
        }

        addressRepository.delete(address);

        return "Address deleted successfully";
    }
}
