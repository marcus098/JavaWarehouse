package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.ProductRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductRestService {
    @Autowired
    private ProductRestRepository productRestRepository;

    public List<Product> getProductsByCategory(long idCategory, long idSupplier){
        return productRestRepository.getProductsByCategory(idCategory, idSupplier);
    }

    public List<Product> getProductsByName(String name, long idSupplier){
        return productRestRepository.getProductsByName(name, idSupplier);
    }

    public List<Product> getProductsByName(String name, double min, long idSupplier){
        return productRestRepository.getProductsByName(name, min, idSupplier);
    }

    public List<Product> getProductsByName(String name, double min, double max, long idSupplier){
        return productRestRepository.getProductsByName(name, min, max, idSupplier);
    }

    public Product moreSell(){
        return productRestRepository.moreSell();
    }
    public Product moreOrder(){
        return productRestRepository.moreOrder();
    }

    public ReturnWithMessage addProduct(String name, String description, double priceSell, int quantity, List<Map<String, Object>> listSupplier){
        return productRestRepository.addProduct(name, description, priceSell, quantity, listSupplier);
    }
}
