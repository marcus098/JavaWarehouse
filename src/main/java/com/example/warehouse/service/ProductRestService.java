package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.ProductRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRestService {
    @Autowired
    private ProductRestRepository productRestRepository;

    public List<Product> getProductsByCategory(long idCategory){
        return productRestRepository.getProductsByCategory(idCategory);
    }

    public List<Product> getProductsByName(String name){
        return productRestRepository.getProductsByName(name);
    }

    public List<Product> getProductsByName(String name, double min){
        return productRestRepository.getProductsByName(name, min);
    }

    public List<Product> getProductsByName(String name, double min, double max){
        return productRestRepository.getProductsByName(name, min, max);
    }

    public Product moreSell(){
        return productRestRepository.moreSell();
    }
    public Product moreOrder(){
        return productRestRepository.moreOrder();
    }

    public ReturnWithMessage addProduct(String name, String description, double priceSell, int quantity){
        return productRestRepository.addProduct(name, description, priceSell, quantity);
    }
}
