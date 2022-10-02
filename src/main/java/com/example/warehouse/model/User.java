package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Role role;
    private long idRole;
    private long id;
    private List<Token> tokenList;

    public User(long id, String email, String name) {
        this.email = email;
        this.id = id;
        this.name = name;
        tokenList = new ArrayList<>();
    }

    public User(String email, String name, String surname, String phone) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.tokenList = new ArrayList<>();
    }

    public User(String email, String name, String surname, String phone, long id) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.id = id;
        this.tokenList = new ArrayList<>();
    }

    public User(String email, String name, String surname, String phone, Role role, long id) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
        this.id = id;
    }

    public User(String email, String password, String name, String surname, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
    }

    public User(String email, String name, String surname, String phone, long idRole, long id) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.idRole = idRole;
        this.id = id;
    }

    public boolean addToken(Token token) {
        return this.tokenList.add(token);
    }

    public int removeToken(String tokenValue) {
        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i).getValue().equals(tokenValue)) {
                tokenList.remove(i);
                break;
            }
        }
        return tokenList.size();
    }

    public boolean checkToken(String token) {
        if (this.tokenList.stream().filter(token1 -> token1.getValue().equals(token)).findFirst().isEmpty())
            return false;
        return true;
    }


    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    
    public List<Token> getTokenList() {
        return tokenList;
    }

    public long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTokenList(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public long getIdRole() {
        return idRole;
    }

    public void setIdRole(long idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", tokenList=" + tokenList +
                '}';
    }
}
