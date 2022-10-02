package com.example.warehouse.controller;

import com.example.warehouse.model.ClientBuy;
import com.example.warehouse.service.OrderRestService;
import com.example.warehouse.service.PurchaseRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
