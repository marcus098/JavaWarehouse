package com.example.warehouse.controller;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.repository.ProductRestRepository;
import com.example.warehouse.repository.UserRestRepository;
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
    @Autowired
    private UserRestRepository userRestRepository;

    @GetMapping("/productscat/{idCategory}/{idSupplier}")
    public List<Product> getProductsByCategory(@PathVariable("idCategory") long idCategory, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByCategory(idCategory, idSupplier);
    }

    @GetMapping("/products/{name}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, idSupplier);
    }

    @GetMapping("/products/{name}/{min}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, min, idSupplier);
    }

    @GetMapping("/products/{name}/{min}/{max}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min, @PathVariable("max") double max, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, min, max, idSupplier);
    }

    @GetMapping("/products/moreOrder")
    public Product moreOrder() {

        return productRestService.moreOrder();
    }

    @GetMapping("/products/moreSell")
    public Product moreSell() {
        return productRestService.moreSell();
    }

    @PostMapping("/products/add")
    public ReturnWithMessage addProduct(@RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("priceSell").toString());
        double priceSell = d.doubleValue();
        Integer i = Integer.parseInt(map.get("quantity").toString());
        int quantity = i.intValue();
        List<Map<String, Object>> listSupplier = (List) map.get("suppliersToSave");
        return productRestService.addProduct(map.get("name").toString(), map.get("description").toString(), priceSell, quantity, listSupplier);
    }

}
