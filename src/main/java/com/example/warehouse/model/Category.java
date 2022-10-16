package com.example.warehouse.model;

import java.util.List;

public class Category {
    private long id;
    private String name;
    private String description;
    private int numberProducts;
    private List<Product> productList; //da implementare

    public Category(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category(long id, String name, String description, int numberProducts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberProducts = numberProducts;
    }

    public Category(String name, String description, int numberProducts) {
        this.name = name;
        this.description = description;
        this.numberProducts = numberProducts;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberProducts() {
        return numberProducts;
    }

    public void setNumberProducts(int numberProducts) {
        this.numberProducts = numberProducts;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
