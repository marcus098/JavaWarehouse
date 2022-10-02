package com.example.warehouse.controller;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.ProductRestRepository;
import com.example.warehouse.service.OrderRestService;
import com.example.warehouse.service.ProductRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ProductRestController {
    @Autowired
    private ProductRestService productRestService;

    @GetMapping("/productscat/{idCategory}")
    public List<Product> getProductsByCategory(@PathVariable("idCategory") long idCategory){
        return productRestService.getProductsByCategory(idCategory);
    }

    @GetMapping("/products/{name}")
    public List<Product> getProductsByName(@PathVariable("name") String name){
        return productRestService.getProductsByName(name);
    }

    @GetMapping("/products/{name}/{min}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min){
        return productRestService.getProductsByName(name, min);
    }

    @GetMapping("/products/{name}/{min}/{max}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min, @PathVariable("max") double max){
        return productRestService.getProductsByName(name, min, max);
    }

    @GetMapping("/products/moreOrder")
    public Product moreOrder(){
        return productRestService.moreOrder();
    }
    @GetMapping("/products/moreSell")
    public Product moreSell(){
        return productRestService.moreSell();
    }

    @PostMapping("/products/add")
    public ReturnWithMessage addProduct(@RequestBody String jsonString){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("priceSell").toString());
        double priceSell = d.doubleValue();
        Integer i = Integer.parseInt(map.get("quantity").toString());
        int quantity = i.intValue();
        return productRestService.addProduct(map.get("name").toString(), map.get("description").toString(), priceSell, quantity);
    }

}
