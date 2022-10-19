package com.example.warehouse.repository;

import com.example.warehouse.DAO.DAORole;
import com.example.warehouse.DAO.DAOUser;
import com.example.warehouse.DAO.DataSource;
import com.example.warehouse.DAO.eccezioni.DAOException;
import com.example.warehouse.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

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
            mapReturn.put("Errore", "campi mancanti");
            return mapReturn;
        }
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            user = daoUser.searchByEmailPassword(connection, email, password);
            if (user != null) {
                mapReturn.put("id", user.getId() + "");
                mapReturn.put("name", user.getName());
                mapReturn.put("surname", user.getSurname());
                mapReturn.put("email", user.getEmail());
                mapReturn.put("idRole", user.getRole().getId() + "");
                //Controllo che il token non esista da nessun utente
                boolean flag = false;
                Token userToken = new Token();
                while (!flag) {
                    flag = true;
                    for (User u : userList) {
                        if (u.checkToken(userToken.getValue())) {
                            flag = false;
                            userToken = new Token();
                            break;
                        }
                    }
                }
                System.out.println(user);
                mapReturn.put("accessToken", userToken.getValue());
                if (userList.stream().filter(user1 -> user1.getId() == user.getId()).findFirst().isEmpty()) {
                    user.addToken(userToken);
                    userList.add(user);
                } else {
                    userList.stream().filter(user1 -> user1.getId() == user.getId()).findFirst().get().addToken(userToken);

                }
            } else {
                mapReturn.put("error", "Credenziali errate");
            }
        } catch (DAOException e) {
            mapReturn.put("error", e.getMessage());
        } finally {
            dataSource.close(connection);
            if (mapReturn.isEmpty())
                mapReturn.put("error", "Credenziali errate");
            return mapReturn;
        }
    }

    public ReturnWithMessage addUser(String name, String surname, String email, String phone, String password, long idRole) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoUser.insert(connection, name, surname, email, phone, password, idRole);
        } catch (DAOException e) {
            //throw new RuntimeException(e);
            return new ReturnWithMessage(false, "Errore");
        }
    }

    public List<User> getUser(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        //List<User> userList = new ArrayList<>();
        return daoUser.getUsers(connection, id);
    }

    public User getUserByToken(String token) {
        Optional<User> userOptional;
        userOptional = userList.stream().filter((user -> user.checkToken(token))).findFirst();
        if (userOptional.isEmpty())
            return null;
        else
            return userOptional.get();
    }

    public int checkToken(String token) {
        Optional<User> userOptional = userList.stream()
                .filter(user -> user.checkToken(token) == true)
                .findFirst();
        System.out.println(token + " ----- ");
        userList.stream().forEach(user -> System.out.println(user.getTokenList()));
        if (userOptional.isEmpty())
            return 1;
        return 0;
    }

    public ReturnWithMessage removeUser(long id) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            return daoUser.delete(connection, id);
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
    }

    public ReturnWithMessage modifyUser(long id, String name, String surname, String email, String phone, long idRole) {
        Connection connection = null;
        connection = dataSource.getConnection();
        try {
            ReturnWithMessage r = daoUser.modifyAll(connection, new User(email, name, surname, phone, idRole, id));
            if (r.isBool()) {
                User user = (User) r.getObject();
                Role role = DAORole.getInstance().searchById(connection, user.getIdRole());
                Optional<User> optionalUser = userList.stream().filter(user1 -> user1.getId() == user.getId()).findFirst();
                if(optionalUser.isPresent()){
                    optionalUser.get().setPhone(user.getPhone());
                    optionalUser.get().setSurname(user.getSurname());
                    optionalUser.get().setName(user.getName());
                    optionalUser.get().setEmail(user.getEmail());
                    optionalUser.get().setRole(role);
                }
                return new ReturnWithMessage(true, "Dati modificati con successo");
            } else {
                return new ReturnWithMessage(false, "Errore");
            }
        } catch (DAOException e) {
            return new ReturnWithMessage(false, "Errore");
        }
    }

    public ReturnWithMessage getRole(String token){
        Role role = userList
                .stream()
                .filter(
                        user -> user.checkToken(token)
                )
                .findFirst().get().getRole();
        return new ReturnWithMessage(true, "", role);
    }

    public ReturnWithMessage checkToken(String token, int page) {
        Optional<User> userOptional = userList.stream()
                .filter(user -> user.checkToken(token) == true)
                .findFirst();
        if (userOptional.isEmpty())
            return new ReturnWithMessage(false, "Logout");
        if (userOptional.get().getRole().getPermissionList().stream().filter(permission -> permission.getId() == page).findFirst().isEmpty())
            return new ReturnWithMessage(false, "Unauthorized");
        return new ReturnWithMessage(true, "Success");
    }

    public int logoutUser(String token) {
        Optional<User> userOptional = userList.stream()
                .filter(user -> user.checkToken(token) == true)
                .findFirst();
        if (!userOptional.isEmpty() && userOptional.get().removeToken(token) == 0) { //return size token user
            userList.remove(userOptional.get());
        }
        userList.stream().forEach(user -> user.getTokenList().stream().forEach(token1 -> System.out.println(token1.getValue())));
        return 0;
    }

    public ReturnWithMessage modifyUser(User user, String password) {
        Optional<User> optionalUser = userList.stream().filter((user1 -> user1.getId() == user.getId())).findFirst();
        Connection connection = null;
        connection = dataSource.getConnection();
        if (optionalUser.isEmpty()) {
            return new ReturnWithMessage(false, "Utente non trovato");
        } else {
            if (!user.getEmail().isEmpty())
                optionalUser.get().setEmail(user.getEmail());
            if (!user.getName().isEmpty())
                optionalUser.get().setName(user.getName());
            if (!user.getSurname().isEmpty())
                optionalUser.get().setSurname(user.getSurname());
            if (!user.getPhone().isEmpty())
                optionalUser.get().setPhone(user.getPhone());
            try {
                if (password == "")
                    return DAOUser.getInstance().modifyAll(connection, optionalUser.get());
                return DAOUser.getInstance().modifyAllPassword(connection, user, password);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
