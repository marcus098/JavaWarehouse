package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAOUser;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.Token;
import com.example.warehouse.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.*;

@Component
public class UserRestRepository {
    private List<User> userList = new ArrayList<>();
    @Autowired
    private DAOUser daoUser;
    @Autowired
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, String> loginUser(String email, String password) {
        User user;
        Map<String, String> mapReturn = new HashMap<>();
        if (email == "" || password == "") {
            mapReturn.put("error", "campi mancanti");
            return mapReturn;
        }
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            user = daoUser.searchByEmailPassword(connection, email, password);
            if (user != null) {
                mapReturn.put("id", user.getId() + "");
                mapReturn.put("name", user.getName());
                mapReturn.put("email", user.getEmail());

                //Controllo che il token non esista da nessun utente
                boolean flag = false;
                Token userToken = new Token();

                while(!flag) {
                    flag = true;
                    for (User u : userList) {
                        if (u.checkToken(userToken.getValue())) {
                            flag = false;
                            userToken = new Token();
                            break;
                        }
                    }
                }
                mapReturn.put("accessToken", userToken.getValue());
                user.addToken(userToken);
                userList.add(user);
            } else {
                mapReturn.put("error", "Credenziali errate");
            }
        } catch (DAOException e) {
            System.out.println(e.getMessage());
            mapReturn.put("error", e.getMessage());
        } finally {
            dataSource.close(connection);
            if (mapReturn.isEmpty())
                mapReturn.put("error", "Credenziali errate");
            return mapReturn;
        }
    }

    public int checkToken(String token) {
        Optional<User> userOptional = userList.stream()
                .filter(user -> user.checkToken(token)==true)
                .findFirst();

        if(userOptional.isEmpty())
            return 1;
        return 0;
    }

    public int logoutUser(String token) {
        Optional<User> userOptional = userList.stream()
                .filter(user -> user.checkToken(token)==true)
                .findFirst();
        if(!userOptional.isEmpty() && userOptional.get().removeToken(token)==0){ //return size token user
            userList.remove(userOptional.get());
        }
        userList.stream().forEach(user -> user.getTokenList().stream().forEach(token1 -> System.out.println(token1.getValue())));
        return 0;
    }
}
