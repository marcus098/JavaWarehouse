package com.example.warehouse.controller;

import com.example.warehouse.model.Cart;
import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.model.ProductPrice;
import com.example.warehouse.service.OrderRestService;
import com.example.warehouse.service.PurchaseRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class PurchaseRestController {

    @Autowired
    private PurchaseRestService purchaseRestService;

    @GetMapping("/purchases")
    public List<ClientBuy> getPurchases(){
        return purchaseRestService.getPurchases();
    }
    @GetMapping("/purchases/{id}")
    public ClientBuy getPurchases(@PathVariable("id") long id){
        return purchaseRestService.getPurchases(id);
    }

    @PostMapping("/sell")
    public boolean sell(@RequestBody Map<String, List<Cart>> cart){
        List<Cart> cartList = cart.get("list");
        System.out.println(cartList);
        return purchaseRestService.sell(cartList);
    }
}
