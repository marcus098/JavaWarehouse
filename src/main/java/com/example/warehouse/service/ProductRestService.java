package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.AutomationRestRepository;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.ProductRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductRestService {
    @Autowired
    private ProductRestRepository productRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;
    @Autowired
    AutomationRestRepository automationRestRepository;

    public boolean checkToken(String token, int page){
        return userRestRepository.checkToken(token, page).isBool();
    }

    public List<Product> getProductsByCategory(long idCategory, long idSupplier){
        List<Product> list = productRestRepository.getProductsByCategory(idCategory, idSupplier);
        list.stream().forEach(product -> product.setDiscount(automationRestRepository.DiscountManagement(product.getId())));
        return list;
    }

    public List<Product> getProductsByName(String name, long idSupplier){
        List<Product> list = productRestRepository.getProductsByName(name, idSupplier);
        list.stream().forEach(product -> product.setDiscount(automationRestRepository.DiscountManagement(product.getId())));
        return list;
    }

    public List<Product> getProductsByName(String name, double min, long idSupplier){
        List<Product> list = productRestRepository.getProductsByName(name, min, idSupplier);
        list.stream().forEach(product -> product.setDiscount(automationRestRepository.DiscountManagement(product.getId())));
        return list;
    }

    public List<Product> getProductsByName(String name, double min, double max, long idSupplier){
        List<Product> list = productRestRepository.getProductsByName(name, min, max, idSupplier);
        list.stream().forEach(product -> product.setDiscount(automationRestRepository.DiscountManagement(product.getId())));
        return list;
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
