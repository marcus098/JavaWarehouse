package com.example.warehouse.model;

public class Statistics {
    private int month;
    private int quantity;
    private int total;

    public Statistics(int month, int quantity, int total) {
        this.month = month;
        this.quantity = quantity;
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMonth() {
        return month;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "month=" + month +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}
