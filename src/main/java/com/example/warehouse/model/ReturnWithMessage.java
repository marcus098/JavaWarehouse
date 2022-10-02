package com.example.warehouse.model;

public class ReturnWithMessage {
    private String message;
    private boolean bool;

    public ReturnWithMessage(boolean bool, String message) {
        this.message = message;
        this.bool = bool;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBool() {
        return bool;
    }

}
