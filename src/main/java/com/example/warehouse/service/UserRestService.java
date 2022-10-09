package com.example.warehouse.service;

import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.User;
import com.example.warehouse.repository.UserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    public ReturnWithMessage addUser(String name, String surname, String email, String phone, String password, long idRole){
        return userRestRepository.addUser(name, surname, email, phone, password, idRole);
    }

    public List<User> getUsers(long id){
        return userRestRepository.getUser(id);
    }

    public User getUserByToken(String token){
        return userRestRepository.getUserByToken(token);
    }

}
