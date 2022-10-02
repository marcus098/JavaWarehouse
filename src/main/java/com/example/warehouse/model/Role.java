package com.example.warehouse.model;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private long id;
    private String name;
    private String description;
    private List<Permission> permissionList;

    public Role(long id, String name, String description, List<Permission> permissionList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissionList = permissionList;
    }

    public Role(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissionList = new ArrayList<>();
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Role(String name, String description, List<Permission> permissionList) {
        this.name = name;
        this.description = description;
        this.permissionList = permissionList;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void removePermission(Permission permission){
        removePermission(permission.getId());
    }
    public void removePermission(long id){
        permissionList.removeIf(permission -> permission.getId()==id);
    }

    public void addPermission(Permission permission){
        if(permissionList.stream().filter(permission1 -> permission1.getId() == permission.getId()).findFirst().isEmpty()){
            permissionList.add(permission);
        }
    }
    private String getPermissionString(){
        String str = "";
        for (Permission p:permissionList)
            str += p.toString();
        return str;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", Permissions: \'" + getPermissionString() + '\'' +
                '}';
    }
}
