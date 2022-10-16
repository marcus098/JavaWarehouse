package com.example.warehouse.service;

import com.example.warehouse.model.Supplier;
import com.example.warehouse.repository.SupplierRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierRestService {
    @Autowired
    SupplierRestRepository supplierRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    public boolean checkToken(String token, int page){
        return userRestRepository.checkToken(token, page).isBool();
    }

    public List<Supplier> getSuppliers(){
        return supplierRestRepository.getSuppliers();
    }

    public List<Supplier> getSuppliersByName(String name){
        return supplierRestRepository.getSuppliersByName(name);
    }

    public boolean addSupplier(String name, String email, String phone, String api){
        return supplierRestRepository.addSupplier(name, email, phone, api);
    }

    public List<Supplier> getProducts(long idProduct) {
        return supplierRestRepository.getProducts(idProduct);
    }
}
