package com.example.warehouse.controller;

import com.example.warehouse.model.Supplier;
import com.example.warehouse.service.SupplierRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
