package com.example.warehouse.model;

public class Cart {
    //private double id;
    private long id;
    private String name;
    //private double quantity;
    private int quantity;
    private double price;

    public Cart(double id, String name, double quantity, double price) {
        this.id = (long) id;
        this.name = name;
        this.quantity = (int) quantity;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
