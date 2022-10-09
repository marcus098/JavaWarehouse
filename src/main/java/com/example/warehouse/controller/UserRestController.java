package com.example.warehouse.controller;

import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.User;
import com.example.warehouse.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class UserRestController {
    @Autowired
    private UserRestService userRestService;

    @PostMapping("/auth")
    public Map<String, String> get(@RequestBody Map<String, String> map) {
        Map prova = userRestService.loginUser(map.get("email"), map.get("password"));
        return prova;
    }

    @PostMapping("/logout")
    public int logout(@RequestBody Map<String, String> token){
        return userRestService.logoutUser(token.get("value"));
    }

    @PostMapping("/check")
    public int checkToken(@RequestBody Map<String, String> value){
        return userRestService.checkToken(value.get("value"));
    }

    @PostMapping("/user/add")
    public ReturnWithMessage addUser(@RequestBody Map<String, String> request){
        if(request.containsKey("name") && request.containsKey("surname") && request.containsKey("phone") && request.containsKey("email") && request.containsKey("password") && request.containsKey("role")){
            if(request.get("name").trim() != "" && request.get("surname").trim() != "" && request.get("phone").trim() != "" && request.get("email").trim() != "" && request.get("password").trim()!= "" && request.get("role").trim() != ""){
                long role = Long.parseLong(request.get("role"));
                return userRestService.addUser(request.get("name"), request.get("surname"), request.get("email"), request.get("phone"), request.get("password"), role);
            }else{
                return new ReturnWithMessage(false, "Presenti dei campi vuoti");
            }
        }else{
            return new ReturnWithMessage(false, "Dati mancanti");
        }
    }

    @PostMapping("/users")
    public List<User> getUsers(@RequestBody Map<String, String> request){
        String token = request.get("token");
        User user = userRestService.getUserByToken(token);
        if(user != null){
            return null;
        }else {
            return userRestService.getUsers(user.getId());
        }
    }
}
