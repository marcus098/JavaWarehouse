package com.example.warehouse.service;

import com.example.warehouse.model.Supplier;
import com.example.warehouse.repository.SupplierRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierRestService {
    @Autowired
    SupplierRestRepository supplierRestRepository;

    public List<Supplier> getSuppliers(){
        return supplierRestRepository.getSuppliers();
    }

    public List<Supplier> getSuppliersByName(String name){
        return supplierRestRepository.getSuppliersByName(name);
    }
}
