package com.example.warehouse.model;

public class Permission {
    private long id;
    private String name;
    private String description;

    public Permission(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
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
        return "Permission{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
