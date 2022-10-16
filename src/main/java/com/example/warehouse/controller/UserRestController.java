package com.example.warehouse.controller;

import com.example.warehouse.model.ReturnWithMessage;
import com.example.warehouse.model.Role;
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

    /* Lista indici pagine:
    * 1) /home
    * 2) /orders
    * 3) /products
    * 4) /account
    * 5) /purchases
    * 6) /AddProduct
    * 7) /AddOrder
    * 8) /AddCategory
    * 9) /usercontrol
    * 10) /suppliers
    * 11) /controlpanel
    * 12) /addaccount
    * 13) /addSupplier
    * 14) /position
    * 15) /automation
    */
    @PostMapping("/auth")
    public Map<String, String> get(@RequestBody Map<String, String> map) {
        Map mapReturn = userRestService.loginUser(map.get("email"), map.get("password"));
        return mapReturn;
    }

    @PostMapping("/logout")
    public int logout(@RequestBody Map<String, String> token) {
        return userRestService.logoutUser(token.get("value"));
    }

    @PostMapping("/check")
    public ReturnWithMessage checkToken(@RequestBody Map<String, String> value) {
        int page = 0;
        if(value.containsKey("page"))
            page = Integer.parseInt(value.get("page"));
        if(page == 0)
            return new ReturnWithMessage(false, "Errore");
        return userRestService.checkToken(value.get("value"), page);
    }

    @PostMapping("/user/add")
    public ReturnWithMessage addUser(@RequestBody Map<String, String> request) {
        if (request.containsKey("name") && request.containsKey("surname") && request.containsKey("phone") && request.containsKey("email") && request.containsKey("password") && request.containsKey("role")) {
            if (request.get("name").trim() != "" && request.get("surname").trim() != "" && request.get("phone").trim() != "" && request.get("email").trim() != "" && request.get("password").trim() != "" && request.get("role").trim() != "") {
                long role = Long.parseLong(request.get("role"));
                return userRestService.addUser(request.get("name"), request.get("surname"), request.get("email"), request.get("phone"), request.get("password"), role);
            } else {
                return new ReturnWithMessage(false, "Presenti dei campi vuoti");
            }
        } else {
            return new ReturnWithMessage(false, "Dati mancanti");
        }
    }

    @PostMapping("/users")
    public List<User> getUsers(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        User user = userRestService.getUserByToken(token);
        if (user == null) {
            return null;
        } else {
            return userRestService.getUsers(user.getId());
        }
    }

    @PostMapping("/getUser")
    public User getUserByToken(@RequestBody Map<String, String> request) {
        String token = "";
        if (request.containsKey("userToken")) {
            token = request.get("userToken");
            return userRestService.getUserByToken(token);
        }
        return null;
    }

    @PostMapping("/user/modify")
    public ReturnWithMessage modifyUser(@RequestBody Map<String, String> request) {
        String token = request.get("userToken");
        String password = "";
        if (request.containsKey("password")) {
            password = request.get("password");
        }
        User user = userRestService.getUserByToken(token);
        String name = request.get("name");
        String surname = request.get("surname");
        String phone = request.get("phone");
        String email = request.get("email");
        if (user.getId() != 0) {
            return userRestService.modifyUser(new User(user.getId(), email, name, surname, phone), password);
        } else {
            return new ReturnWithMessage(false, "Errore utente");
        }
    }
}
