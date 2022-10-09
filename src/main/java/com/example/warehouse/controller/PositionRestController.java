package com.example.warehouse.controller;

import com.example.warehouse.model.Position;
import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.service.PositionRestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class PositionRestController {
    @Autowired
    PositionRestService positionRestService;

    @GetMapping("/position/{name}/{id}")
    public List<Position> searchPositionByName(@PathVariable("name") String name, @PathVariable("id") long idProduct) {
        return positionRestService.searchPositionByName(name, idProduct);
    }
    @GetMapping("/position/{id}")
    public List<Position> searchPositionByIdProduct(@PathVariable("id") long idProduct) {
        return positionRestService.searchPositionByIdProduct(idProduct);
    }

    @GetMapping("/positions")
    public List<Position> getPositions(){
        return positionRestService.getPositions();
    }

    @PostMapping("/position/add")
    public ReturnWithMessage addPosition(@RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, String> map = mapJson;
        if (map.containsKey("name"))
            return positionRestService.addPosition(new Position(map.get("name"), map.get("description")));
        return new ReturnWithMessage(false, "Dati mancanti");
    }
    @PostMapping("/position/updateProduct")
    public ReturnWithMessage updateProductPosition(@RequestBody String jsonString) {
        Gson gson = new Gson();
        Map mapJson = gson.fromJson(jsonString, Map.class);
        Map<String, Long> map = mapJson;
        if (map.containsKey("idPosition")) {
            if (map.containsKey("idProduct"))
                return positionRestService.updateProductPosition(map.get("idPosition"), map.get("idProduct"));
            return positionRestService.updateProductPositionNull(map.get("idPosition"));
        }
        return new ReturnWithMessage(false, "Dati mancanti");
    }

    @PostMapping("/position/delete/{id}")
    public boolean deletePosition(@PathVariable("id") long id){
        return positionRestService.deletePosition(id);
    }

    @PostMapping("/position/empty/{id}")
    public boolean emptyPosition(@PathVariable("id") long id){
        return positionRestService.emptyPosition(id);
    }

}
