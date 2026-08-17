package com.example.Food_Delivery.Service;

import com.example.Food_Delivery.DTO.Food.Message;
import com.example.Food_Delivery.DTO.Shop.ShopRequest;
import com.example.Food_Delivery.DTO.Shop.ShopResponse;
import com.example.Food_Delivery.Model.Shop;
import com.example.Food_Delivery.Model.ShopStatus;
import com.example.Food_Delivery.Repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopResponse createRestaurant(ShopRequest shopRequest){
        Shop shop = Shop.builder()
                .shopName(shopRequest.getShopName())
                .ownerName(shopRequest.getOwnerName())
                .ownerEmail(shopRequest.getOwnerEmail())
                .description(shopRequest.getDescription())
                .build();
        Shop savedShop =shopRepository.save(shop);
        return mapToResponse(savedShop);
    }
    public ShopResponse mapToResponse(Shop savedShop){
        ShopResponse shopResponse = ShopResponse.builder()
                .shopId(savedShop.getShopId())
                .ShopName(savedShop.getShopName())
                .ownerName(savedShop.getOwnerName())
                .ownerEmail(savedShop.getOwnerEmail())
                .description(savedShop.getDescription())
                .rating(savedShop.getRating())
                .build();
        return shopResponse;
    }

    public ShopResponse updateRestaurant( ShopRequest shopRequest) {
        Shop shop = shopRepository.findByOwnerEmail(shopRequest.getOwnerEmail())
                .orElseThrow(()-> new RuntimeException("Email doesn't Exist"));
        shop.setShopName(shopRequest.getShopName());
        shop.setOwnerName(shopRequest.getOwnerName());
        shop.setOwnerEmail(shopRequest.getOwnerEmail());
        shop.setDescription(shopRequest.getDescription());
        Shop updatedShop = shopRepository.save(shop);
        return mapToResponse(updatedShop);
    }

    public ShopResponse readRestaurant(String ownerEmail) {
        Shop shop = shopRepository.findByOwnerEmail(ownerEmail)
                .orElseThrow(()-> new RuntimeException("Email Mismatch"));
        return mapToResponse(shop);
    }


    public Message deleteRestaurant(String ownerEmail) {
        Shop shop = shopRepository.findByOwnerEmail(ownerEmail)
                .orElseThrow(()-> new RuntimeException("Owner Doesn't Exist"));
        if (shop.getShopStatus() == ShopStatus.INACTIVE){
            throw new RuntimeException("Owner Already Inactive");
        }
        if (shop.getShopStatus() == ShopStatus.BLOCKED){
            throw new RuntimeException("Owner is Blocked. So Owner can't delete their account");
        }
        if (shop.getShopStatus() == ShopStatus.ACTIVE){
            shop.setShopStatus(ShopStatus.INACTIVE);
            shopRepository.save(shop);
        }
        return new Message("ShopOwner account is Successfully Deleted");
    }


    public Message ReActiveOwner(String email) {
        Shop shop = shopRepository.findByOwnerEmail(email)
                .orElseThrow(()-> new RuntimeException("Email doesn't Match"));

        if (shop.getShopStatus() == ShopStatus.ACTIVE){
            throw new RuntimeException("Owner is Already Active");
        }
        if (shop.getShopStatus() == ShopStatus.BLOCKED){
            throw new RuntimeException("Owner is Blocked. So Owner can't Activate their account");
        }
        if (shop.getShopStatus() == ShopStatus.INACTIVE){
            shop.setShopStatus(ShopStatus.ACTIVE);
            shopRepository.save(shop);
        }
        return new  Message("Owner get Activated");
    }
}
