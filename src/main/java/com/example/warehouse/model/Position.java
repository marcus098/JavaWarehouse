package com.example.warehouse.model;

public class Position {
    private long id;
    private String name;
    private String description;
    private Product product;

    public Position(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Position(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Position(long id, String name, String description, Product product) {
        this.id = id;
        this.name = name;
        this.product = product;
        this.description = description;
    }

    public Position(String name, String description, Product product) {
        this.name = name;
        this.description = description;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
