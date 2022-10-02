package com.example.warehouse.model;

import com.example.warehouse.DAO.DAOOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private long id;
    private String description;
    private LocalDateTime date;
    private double total;
    private int quantity;
    private Product product;
    private Supplier supplier;
    private long idSupplier;

    private DAOOrder daoOrder;

    public Order(long id, String description, LocalDateTime date, double total, int quantity, Product product) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.total = total;
        this.quantity = quantity;
        this.product = product;
    }
    public Order(long id, String description, LocalDateTime date, double total, int quantity, Product product, long idSupplier) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.total = total;
        this.quantity = quantity;
        this.product = product;
        this.idSupplier = idSupplier;
    }
    public Order(long id, String description, LocalDateTime date, double total, int quantity, Product product, Supplier supplier) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.total = total;
        this.quantity = quantity;
        this.product = product;
        this.supplier = supplier;
    }

    public Order(String description, LocalDateTime date, double total, int quantity, Product product) {
        this.description = description;
        this.date = date;
        this.total = total;
        this.quantity = quantity;
        this.product = product;
    }

    public long getId() {
        return id;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public Product getProduct() {
        return product;
    }

    public double getTotal() {
        return total;
    }

    public String getDate() {
        return date.toString().replace("T", " ");
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "Order{" +
                "description='" + description + '\'' +
                ", date=" + date.toString().replace("T", " ") +
                ", total=" + total +
                ", quantity=" + quantity +
                ", product=" + product +
                ", supplier=" + supplier +
                '}';
    }
}
