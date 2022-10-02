package com.example.warehouse.service;

import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRestService {
    @Autowired
    private UserRestRepository userRestRepository;

    public Map<String, String> loginUser(String email, String password){
        Map<String, String> mapReturn =  userRestRepository.loginUser(email, password);
        if(mapReturn.containsKey("error")){
            String message = mapReturn.get("error");
            mapReturn = new HashMap<>();
            mapReturn.put("accessToken", "0");
            mapReturn.put("roles", "0");
            mapReturn.put("message", message);
        }
        return mapReturn;
    }

    public int logoutUser(String token){
        return userRestRepository.logoutUser(token);
    }
    public int checkToken(String token){
        return userRestRepository.checkToken(token);
    }

}
