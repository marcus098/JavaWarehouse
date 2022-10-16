package com.example.warehouse.model;

import java.util.List;

public class ReturnWithMessage {
    private String message;
    private boolean bool;
    private Object object;
    private List<Object> list;

    public ReturnWithMessage(boolean bool, String message) {
        this.message = message;
        this.bool = bool;
    }

    public ReturnWithMessage(boolean bool, String message, Object object) {
        this.message = message;
        this.bool = bool;
        this.object = object;
    }

    public ReturnWithMessage(boolean bool, String message, List<Object> list) {
        this.message = message;
        this.bool = bool;
        this.list = list;
    }

    public List<Object> getList() {
        return list;
    }

    public Object getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBool() {
        return bool;
    }

    @Override
    public String toString() {
        return "ReturnWithMessage{" +
                "message='" + message + '\'' +
                ", bool=" + bool +
                ", object=" + object +
                '}';
    }
}
