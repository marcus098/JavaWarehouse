package com.example.warehouse.controller;

import com.example.warehouse.model.*;
import com.example.warehouse.service.OrderRestService;
import com.example.warehouse.service.PurchaseRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class PurchaseRestController {
    @Autowired
    private PurchaseRestService purchaseRestService;
    @CrossOrigin
    @GetMapping("/purchases")
    public List<ClientBuy> getPurchases(@RequestHeader(value = "userToken") String token) {
        if (purchaseRestService.checkToken(token, 5))
            return purchaseRestService.getPurchases();
        return new ArrayList<>();
    }
    @CrossOrigin
    @GetMapping("/purchases/{id}")
    public ClientBuy getPurchases(@PathVariable("id") long id, @RequestHeader(value = "userToken") String token) {
        if (purchaseRestService.checkToken(token, 5))
            return purchaseRestService.getPurchases(id);
        return null;
    }
    @CrossOrigin
    @PostMapping("/sell")
    public ReturnWithMessage sell(@RequestBody Map<String, List<Cart>> cart, @RequestHeader(value = "userToken") String token,@RequestHeader(value = "description") String description /*@RequestHeader Map<String, String> headers*/) {
        if (purchaseRestService.checkToken(token, 5)) {
            List<Cart> cartList = cart.get("list");
            System.out.println(cartList);
            return purchaseRestService.sell(cartList, description);
        } else {
            return new ReturnWithMessage(false, "Errore Utente");
        }
    }
    @CrossOrigin
    @GetMapping("/sells/Months")
    public List<Statistics> searchQuantitySellsMonths() {
        return purchaseRestService.searchQuantitySellsMonths();
    }
}
