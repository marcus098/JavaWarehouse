package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private long id;
    private String name;
    private String description;
    private double priceSell;
    private int quantity;
    private double discount;
    private List<Supplier> supplierList; // da inizializzare
    private List<Position> positionList = new ArrayList<>();
    private Category category;

    public Product(long id, String name, String description, double priceSell, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
    }

    public Product(String name, String description, double priceSell, int quantity) {
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
    }

    public Product(long id, String name, String description, double priceSell, int quantity, List<Position> positionList, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.positionList = positionList;
        this.category = category;
    }

    public Product(String name, String description, double priceSell, int quantity, List<Position> positionList, Category category) {
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.positionList = positionList;
        this.category = category;
    }

    //Discount

    public Product(long id, String name, String description, double priceSell, int quantity, double discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Product(String name, String description, double priceSell, int quantity, double discount) {
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Product(long id, String name, String description, double priceSell, int quantity, List<Position> positionList, Category category, double discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.positionList = positionList;
        this.category = category;
        this.discount = discount;
    }

    public Product(String name, String description, double priceSell, int quantity, List<Position> positionList, Category category, double discount) {
        this.name = name;
        this.description = description;
        this.priceSell = priceSell;
        this.quantity = quantity;
        this.positionList = positionList;
        this.category = category;
        this.discount = discount;
    }
    //discount fine


    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceSell() {
        return priceSell;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriceSell(double priceSell) {
        this.priceSell = priceSell;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setPositionList(List<Position> positionList) {
        System.out.println("set");
        this.positionList = positionList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priceSell=" + priceSell +
                ", quantity=" + quantity +
                ", category=" + category +
                '}';
    }
}
