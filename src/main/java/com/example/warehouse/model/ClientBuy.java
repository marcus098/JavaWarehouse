package com.example.warehouse.model;

import java.time.LocalDateTime;
import java.util.List;

public class ClientBuy {
    private long id;
    private String description;
    private int quantity;
    private List<ProductPrice> productPriceList;
    private LocalDateTime localDateTime;
    private double price;

    public ClientBuy(long id, String description, List<ProductPrice> productPriceList, LocalDateTime localDateTime) {
        this.id = id;
        this.description = description;
        this.productPriceList = productPriceList;
        this.localDateTime = localDateTime;
        this.quantity = productPriceList.stream().mapToInt(p -> p.getQuantity()).sum();
        this.price = productPriceList.stream().mapToDouble(p -> p.getPrice()).sum();
    }

    public ClientBuy(String description, List<ProductPrice> productPriceList, LocalDateTime localDateTime) {
        this.description = description;
        this.productPriceList = productPriceList;
        this.localDateTime = localDateTime;
        this.quantity = productPriceList.stream().mapToInt(p -> p.getQuantity()).sum();
        this.price = productPriceList.stream().mapToDouble(p -> p.getPrice()).sum();
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public List<ProductPrice> getProductPriceList() {
        return productPriceList;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void addProductPrice(ProductPrice productPrice){
        productPriceList.add(productPrice);
    }

    public void removeProductPrice(ProductPrice productPrice){
        removeProductPrice(productPrice.getId());
    }

    public void removeProductPrice(long id){
        productPriceList.removeIf(productPrice1 -> productPrice1.getId() == id);
    }

    @Override
    public String toString() {
        return "ClientBuy{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", localDateTime=" + localDateTime +
                ", price=" + price +
                '}';
    }
}
