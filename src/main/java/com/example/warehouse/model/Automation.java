package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Automation {
    private long id;
    private String name;
    private boolean active;
    private List<StocksRules> stocksRulesList = new ArrayList<>();
    private List<DiscountRules> discountRulesList = new ArrayList<>();

    public Automation(long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }


    public Automation(long id, String name, boolean active, List<StocksRules> stocksRulesList) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.stocksRulesList = stocksRulesList;
    }


    public Automation(String name, long id, boolean active, List<DiscountRules> discountRulesList) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.discountRulesList = discountRulesList;
    }

    public Automation(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public boolean addStockRule(StocksRules stocksRules) {
        return (this.stocksRulesList.add(stocksRules));
    }

    public boolean addDiscountRule(DiscountRules discountRules) {
        return (this.discountRulesList.add(discountRules));
    }

    public boolean removeDiscountRule(DiscountRules discountRules) {
        if (discountRulesList.size() == 0)
            return false;
        Optional<DiscountRules> optionalDiscountRules = discountRulesList.stream().findFirst().stream().filter(discountRules1 -> discountRules1.getId() == discountRules.getId()).findFirst();
        if (optionalDiscountRules.isEmpty())
            return false;
        return (discountRulesList.remove(optionalDiscountRules.get()));
    }
    public boolean removeStockRule(StocksRules stocksRules) {
        if (stocksRulesList.size() == 0)
            return false;
        Optional<StocksRules> optionalStockRules = stocksRulesList.stream().findFirst().stream().filter(stocksRules1 -> stocksRules1.getId() == stocksRules.getId()).findFirst();
        if (optionalStockRules.isEmpty())
            return false;
        return (discountRulesList.remove(optionalStockRules.get()));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<DiscountRules> getDiscountRulesList() {
        return discountRulesList;
    }

    public List<StocksRules> getStocksRulesList() {
        return stocksRulesList;
    }

    @Override
    public String toString() {
        return "Automation{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", active=" + active +
                ", discountList=" + discountRulesList +
                ", stockList=" + stocksRulesList +
                '}';
    }
}
