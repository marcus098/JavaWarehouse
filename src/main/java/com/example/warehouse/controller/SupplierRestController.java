package com.example.warehouse.controller;

import com.example.warehouse.model.ReturnWithMessage;
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

    @CrossOrigin
    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers() {
        return supplierRestService.getSuppliers();
    }

    @GetMapping("/suppliers/search/{name}")
    public List<Supplier> getSuppliersByName(@PathVariable("name") String name) {
        return supplierRestService.getSuppliersByName(name);
    }

    @CrossOrigin
    @PostMapping("/suppliers/add")
    public boolean addSupplier(@RequestBody Map<String, String> request) {
        String api = "";
        if (request.containsKey("api"))
            api = request.get("api");
        if (supplierRestService.checkToken(request.get("userToken"), 13)) {
            return supplierRestService.addSupplier(request.get("name"), request.get("email"), request.get("phone"), api);
        }
        return false;
    }

    @CrossOrigin
    @PostMapping("/suppliers/getProduct/{idProduct}")
    public List<Supplier> getProducts(@PathVariable("idProduct") long idProduct, @RequestBody Map<String, String> token) {
        return supplierRestService.getProducts(idProduct);
    }

    @CrossOrigin
    @PostMapping("/supplier/modify/{id}")
    public ReturnWithMessage modifyUser(@PathVariable("id") long idSupplier, @RequestBody Map<String, String> request) {
        String token = request.get("userToken");
        String name = request.get("name");
        String email = request.get("email");
        String phone = request.get("phone");
        String api = request.get("api");
        if (supplierRestService.checkToken(token, 13))
            return supplierRestService.modifySupplier(idSupplier, name, email, phone, api);
        return new ReturnWithMessage(false, "Unauthorized");
    }

    @CrossOrigin
    @GetMapping("/supplier/delete/{id}")
    public ReturnWithMessage removeSupplier(@PathVariable("id") long id, @RequestHeader(value = "userToken") String token) {
        if (supplierRestService.checkToken(token, 13))
            return supplierRestService.removeSupplier(id);
        return new ReturnWithMessage(false, "Unauthorized");
    }
}
