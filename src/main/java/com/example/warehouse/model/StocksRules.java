package com.example.warehouse.model;

public class StocksRules {
    private long id;
    private boolean minMax;
    private long number;
    private Automation automation;

    public StocksRules(long id, boolean minMax, long number) {
        this.id = id;
        this.minMax = minMax;
        this.number = number;
        this.automation = automation;
    }

    public StocksRules(boolean minMax, long number) {
        this.minMax = minMax;
        this.number = number;
        this.automation = automation;
    }

    public long getId() {
        return id;
    }

    public long getNumber() {
        return number;
    }

    public Automation getAutomation() {
        return automation;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public void setMinMax(boolean minMax) {
        this.minMax = minMax;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StocksRules{" +
                "id=" + id +
                ", minMax=" + minMax +
                ", number=" + number +
                ", automation=" + automation +
                '}';
    }
}
