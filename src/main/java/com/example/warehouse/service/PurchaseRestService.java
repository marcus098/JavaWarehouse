package com.example.warehouse.service;

import com.example.warehouse.model.Cart;
import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.PurchaseRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseRestService {
    @Autowired
    private PurchaseRestRepository purchaseRestRepository;

    public List<ClientBuy> getPurchases(){
        return purchaseRestRepository.getPurchases();
    }

    public ClientBuy getPurchases(long id){
        return purchaseRestRepository.getPurchases(id);
    }

    public boolean sell(List<Cart> cartList){
        return purchaseRestRepository.sell(cartList);
    }
}
