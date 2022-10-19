package com.example.warehouse.controller;

import com.example.warehouse.model.Supplier;
import com.example.warehouse.service.SupplierRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class SupplierRestController {
    @Autowired
    SupplierRestService supplierRestService;

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers(){
        return supplierRestService.getSuppliers();
    }

    @GetMapping("/suppliers/search/{name}")
    public List<Supplier> getSuppliersByName(@PathVariable("name") String name){
        return supplierRestService.getSuppliersByName(name);
    }

    @PostMapping("/suppliers/add")
    public boolean addSupplier(@RequestBody Map<String, String> request){
        String api = "";
        if(request.containsKey("api"))
            api = request.get("api");
        if(supplierRestService.checkToken(request.get("userToken"), 13)) {
            return supplierRestService.addSupplier(request.get("name"), request.get("email"), request.get("phone"), api);
        }
        return false;
    }

    @PostMapping("/suppliers/getProduct/{idProduct}")
    public List<Supplier> getProducts(@PathVariable ("idProduct") long idProduct, @RequestBody Map<String, String> token){
        return supplierRestService.getProducts(idProduct);
    }
}
