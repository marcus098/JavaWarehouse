package com.example.warehouse.model;

public class ProductPrice {
    private Product product;
    private double price;
    private double discount;
    private long id;
    private long idProduct;
    private int quantity;

    public ProductPrice(Product product, double price, double discount, int quantity) {
        this.product = product;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    public ProductPrice(long id, Product product, double price, double discount, int quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
        this.discount = discount;
    }

    public ProductPrice(long idProduct, String name, int quantity,double price/*, double discount*/) {
        this.price = price;
        //this.discount = discount;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public long getId() {
        return id;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
                "product=" + product +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
