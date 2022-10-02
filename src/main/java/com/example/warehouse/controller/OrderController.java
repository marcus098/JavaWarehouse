package com.example.warehouse.controller;

import com.example.warehouse.model.*;
import com.example.warehouse.service.OrderRestService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

    @GetMapping("/orders")
    public List<Order> searchOrders(){
        /*Order order = orderRestService.searchOrders().get(0);
        List<Order> list = orderRestService.searchOrders();
        for(int i = 0; i < 20; i++){
            for(Order o : orderRestService.searchOrders())
                list.add(o);
        }*/
        return orderRestService.searchOrders();

       // return list;
    }

    @PostMapping("/orders/delete")
    public ReturnWithMessage deleteOrder(@RequestBody String jsonString){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("id").toString());
        Long l = d.longValue();
        if(map.containsKey("id"))
            return orderRestService.deleteOrder(l);
        return new ReturnWithMessage(false, "Id non ricevuto dal server");
    }

    @PostMapping("/orders/confirm/{id}/{idPosition}")
    public ReturnWithMessage confirmOrder(@PathVariable("id") long id, @PathVariable("idPosition") long idPosition, @RequestBody String jsonString){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        return orderRestService.confirmOrder(id, idPosition);
    }

    @PostMapping("/orders/confirmNew/{id}")
    public ReturnWithMessage confirmOrder(@PathVariable("id") long id, @RequestBody String jsonString){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, String> map = mapJson;
        Position position = new Position(map.get("name"), map.get("description"));
        return orderRestService.confirmOrder(id, position);
    }

    @GetMapping("/orders/Months")
    public List<Statistics> searchQuantityOrdersMonths2() {
        return orderRestService.searchQuantityOrdersMonths();
    }

    @PostMapping("/orders/add")
    public ReturnWithMessage addOrder(@RequestBody String jsonString){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("total").toString());
        double total = d.doubleValue();
        Integer i = Integer.parseInt(map.get("quantity").toString());
        int quantity = i.intValue();
        Long l = Long.parseLong(map.get("idProductSupplier").toString());
        long idProductSupplier = l.longValue();
        return orderRestService.addOrder(total,map.get("description").toString(), idProductSupplier, quantity);
    }


}
