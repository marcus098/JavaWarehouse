package com.example.warehouse.model;

public class Supplier {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String api;

    public Supplier(long id, String name, String email, String phone, String api) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.api = api;
    }

    public Supplier(String name, String email, String phone, String api) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.api = api;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getApi() {
        return api;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApi(String api) {
        this.api = api;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", api='" + api + '\'' +
                '}';
    }
}
