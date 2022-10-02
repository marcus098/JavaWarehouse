package com.example.warehouse.model;

public class Automation {
    private long id;
    private String name;
    private boolean active;

    public Automation(long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Automation(String name, boolean active) {
        this.name = name;
        this.active = active;
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

    @Override
    public String toString() {
        return "Automation{" +
                "name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
