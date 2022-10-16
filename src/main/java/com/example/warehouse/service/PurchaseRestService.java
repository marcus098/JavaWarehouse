package com.example.warehouse.service;

import com.example.warehouse.model.Cart;
import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Statistics;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.PurchaseRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseRestService {
    @Autowired
    private PurchaseRestRepository purchaseRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    public boolean checkToken(String token, int page){
        return userRestRepository.checkToken(token, page).isBool();
    }

    public List<ClientBuy> getPurchases() {
        return purchaseRestRepository.getPurchases();
    }

    public ClientBuy getPurchases(long id) {
        return purchaseRestRepository.getPurchases(id);
    }

    public ReturnWithMessage sell(List<Cart> cartList) {
        if(purchaseRestRepository.sell(cartList))
            return new ReturnWithMessage(true, "Vendita registrata con successo");
        return new ReturnWithMessage(false, "Errore vendita");
    }

    public List<Statistics> searchQuantitySellsMonths() {
        List<Statistics> list = purchaseRestRepository.searchQuantitySellsMonths();
        List<Statistics> newList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int integ = i;
            Optional<Statistics> optionalStatistics = list.stream().filter(statistics -> statistics.getMonth() == integ).findFirst();
            if (optionalStatistics.isEmpty())
                newList.add(new Statistics(i, 0, 0));
            else
                newList.add(optionalStatistics.get());
        }
        return newList;

    }
}
