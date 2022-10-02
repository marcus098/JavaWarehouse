package com.example.warehouse.controller;

import com.example.warehouse.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
