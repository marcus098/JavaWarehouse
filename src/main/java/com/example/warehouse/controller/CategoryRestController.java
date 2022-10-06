package com.example.warehouse.controller;

import com.example.warehouse.model.Category;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.service.CategoryRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class CategoryRestController {
    @Autowired
    private CategoryRestService categoryRestService;

    @GetMapping("/categories/{idSupplier}")
    public List<Category> getCategories(@PathVariable long idSupplier){
        //System.out.println(idSupplier);
        return categoryRestService.getCategories(idSupplier);
    }

    @PostMapping("/categories/add/")
    public ReturnWithMessage addCategory(@RequestBody String jsonString, @PathVariable long idSupplier){
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, String> map = mapJson;
        return categoryRestService.addCategory(map.get("name"), map.get("description"));
    }

    @GetMapping("/categories/delete/{id}")
    public boolean deleteCategory(@RequestBody long id){
        return categoryRestService.deleteCategory(id);
    }

}
