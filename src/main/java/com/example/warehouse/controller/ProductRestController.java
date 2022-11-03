package com.example.warehouse.controller;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.repository.ProductRestRepository;
import com.example.warehouse.repository.UserRestRepository;
import com.example.warehouse.service.OrderRestService;
import com.example.warehouse.service.ProductRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class ProductRestController {
    @Autowired
    private ProductRestService productRestService;
    @Autowired
    private UserRestRepository userRestRepository;
    @CrossOrigin
    @GetMapping("/productscat/{idCategory}/{idSupplier}")
    public List<Product> getProductsByCategory(@PathVariable("idCategory") long idCategory, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByCategory(idCategory, idSupplier);
    }
    @CrossOrigin
    @GetMapping("/products/{name}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, idSupplier);
    }
    @CrossOrigin
    @GetMapping("/products/{name}/{min}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, min, idSupplier);
    }
    @CrossOrigin
    @GetMapping("/products/{name}/{min}/{max}/{idSupplier}")
    public List<Product> getProductsByName(@PathVariable("name") String name, @PathVariable("min") double min, @PathVariable("max") double max, @PathVariable("idSupplier") long idSupplier) {
        return productRestService.getProductsByName(name, min, max, idSupplier);
    }
    @CrossOrigin
    @GetMapping("/products/moreOrder")
    public Product moreOrder() {

        return productRestService.moreOrder();
    }
    @CrossOrigin
    @GetMapping("/products/moreSell")
    public Product moreSell() {
        return productRestService.moreSell();
    }
    @CrossOrigin
    @PostMapping("/products/add")
    public ReturnWithMessage addProduct(@RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Object> map = mapJson;
        Double d = Double.parseDouble(map.get("priceSell").toString());
        double priceSell = d.doubleValue();
        Integer i = Integer.parseInt(map.get("quantity").toString());
        int quantity = i.intValue();
        Double d1 = Double.parseDouble(map.get("category").toString());
        long idCategory = d1.longValue();
        List<Map<String, Object>> listSupplier = (List) map.get("suppliersToSave");
        String token = map.get("userToken").toString();
        if(productRestService.checkToken(token, 6))
            return productRestService.addProduct(map.get("name").toString(), map.get("description").toString(), priceSell, quantity, listSupplier, idCategory);
        return new ReturnWithMessage(false, "Non hai i permessi");
    }
    @CrossOrigin
    @PostMapping("/product/delete/{id}")
    public ReturnWithMessage deleteProduct(@PathVariable("id") long id, @RequestBody Map<String, String> request){
        if(productRestService.checkToken(request.get("userToken"), 6)){
            return productRestService.deleteProduct(id);
        }
        return new ReturnWithMessage(false, "Non hai i permessi");
    }

}
