package com.example.warehouse.service;

import com.example.warehouse.model.Automation;
import com.example.warehouse.model.Category;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.repository.CategoryRestRepository;
import com.example.warehouse.repository.OrderRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryRestService {
    @Autowired
    private CategoryRestRepository categoryRestRepository;
    @Autowired
    private UserRestRepository userRestRepository;

    public boolean checkToken(String token, int page){
        return userRestRepository.checkToken(token, page).isBool();
    }

    public List<Category> getCategories(long idSupplier) {
        return categoryRestRepository.getCategories(idSupplier);
    }

    public ReturnWithMessage addCategory(String name, String description){
        return categoryRestRepository.addCategory(name, description);
    }

    public boolean deleteCategory(long id){
        return categoryRestRepository.deleteCategory(id);
    }


}
