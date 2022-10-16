package com.example.warehouse.model;

import org.springframework.beans.factory.annotation.Autowired;

public class DiscountRules {
    private long id;
    private double percentage;
    private int period;
    private int typePeriod; //0:giorni, 1: mesi, 2:anni
    private Automation automation;

    public DiscountRules(long id, double percentage, int period, int typePeriod) {
        this.id = id;
        this.percentage = percentage;
        this.period = period;
        this.typePeriod = typePeriod;
        this.automation = automation;
    }

    public DiscountRules(double percentage, int period, int typePeriod) {
        this.percentage = percentage;
        this.period = period;
        this.typePeriod = typePeriod;
        this.automation = automation;
    }

    public Automation getAutomation() {
        return automation;
    }

    public long getId() {
        return id;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getPeriod() {
        return period;
    }

    public void setAutomation(Automation automation) {
        this.automation = automation;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setTypePeriod(int typePeriod) {
        this.typePeriod = typePeriod;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTypePeriod() {
        return typePeriod;
    }

    @Override
    public String toString() {
        return "DiscountRules{" +
                "id=" + id +
                ", percentage=" + percentage +
                ", period=" + period +
                ", typePeriod=" + typePeriod +
                ", automation=" + automation +
                '}';
    }
}
