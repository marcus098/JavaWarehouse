package com.example.warehouse.controller;

import com.example.warehouse.model.*;
import com.example.warehouse.service.OrderRestService;
import com.google.gson.*;
/*
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class OrderController {

    @Autowired
    private OrderRestService orderRestService;
    @CrossOrigin
    @GetMapping("/orders")
    public List<Order> searchOrders(@RequestHeader(value="userToken") String token) {
        if (orderRestService.checkToken(token, 2)) {
            return orderRestService.searchOrders();
        } else {
            return new ArrayList<>();
        }
    }
    @CrossOrigin
    @PostMapping("/orders/delete")
    public ReturnWithMessage deleteOrder(@RequestBody String jsonString/*, @RequestHeader(value="userToken") String token*/) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("id").toString());
        Long l = d.longValue();
        String token = map.get("userToken").toString();
        if (orderRestService.checkToken(token, 2)) {
            if (map.containsKey("id"))
                return orderRestService.deleteOrder(l);
            return new ReturnWithMessage(false, "Id non ricevuto dal server");
        }else{
            return new ReturnWithMessage(false, "Non hai l'autorizzazione");
        }
    }
    @CrossOrigin
    @PostMapping("/orders/confirm/{id}/{idPosition}")
    public ReturnWithMessage confirmOrder(@PathVariable("id") long id, @PathVariable("idPosition") long idPosition, @RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        return orderRestService.confirmOrder(id, idPosition);
    }
    @CrossOrigin
    @PostMapping("/orders/confirmNew/{id}")
    public ReturnWithMessage confirmOrder(@PathVariable("id") long id, @RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, String> map = mapJson;
        Position position = new Position(map.get("name"), map.get("description"));
        return orderRestService.confirmOrder(id, position);
    }
    @CrossOrigin
    @GetMapping("/orders/Months")
    public List<Statistics> searchQuantityOrdersMonths2() {
        return orderRestService.searchQuantityOrdersMonths();
    }
    @CrossOrigin
    @PostMapping("/orders/add")
    public ReturnWithMessage addOrder(@RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Integer i = Integer.parseInt(map.get("quantity").toString());
        int quantity = i.intValue();
        Long l = Long.parseLong(map.get("idSupplier").toString());
        long idSupplier = l.longValue();
        //l = Long.parseLong(map.get("idProduct").toString());
        Double d = Double.parseDouble(map.get("idProduct").toString());
        long idProduct = d.longValue();
        return orderRestService.addOrder(map.get("description").toString(), idSupplier, idProduct, quantity);
    }
}
