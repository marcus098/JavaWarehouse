package com.example.warehouse.controller;

import com.example.warehouse.model.Category;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.service.CategoryRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class CategoryRestController {
    @Autowired
    private CategoryRestService categoryRestService;

    @GetMapping("/categories/{idSupplier}")
    public List<Category> getCategories(@PathVariable long idSupplier, @RequestHeader(value = "userToken") String token) {
        //System.out.println(idSupplier);
        if (categoryRestService.checkToken(token, 3)) {
            return categoryRestService.getCategories(idSupplier);
        }
        return new ArrayList<>();
    }

    @PostMapping("/categories/add")
    public ReturnWithMessage addCategory(@RequestBody String jsonString) {
        System.out.println(jsonString);
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, String> map = mapJson;
        if (categoryRestService.checkToken(map.get("userToken"), 8)) {
            return categoryRestService.addCategory(map.get("name"), map.get("description"));
        }
        return new ReturnWithMessage(false, "Non hai i permessi");
    }

    @GetMapping("/categories/delete/{id}")
    public boolean deleteCategory(@PathVariable("id") long id, @RequestHeader(value = "userToken") String token) {
        if (categoryRestService.checkToken(token, 8))
            return categoryRestService.deleteCategory(id);
        return false;
    }

}
