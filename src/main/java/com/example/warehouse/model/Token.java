package com.example.warehouse.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

public class Token {
    private String value;
    private LocalDateTime dateTime;

    public Token(){
        this.value = createToken();
        this.dateTime = LocalDateTime.now();
    }
    public Token(String value){
        this.value = value;
    }

    private String createToken(){
        String chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ?!$%&)(";
        SecureRandom secureRandom = null;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String customTag = secureRandom.ints(64, 0, chrs.length()).mapToObj(i -> chrs.charAt(i))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
        return customTag;
    }
    public String getValue() {
        return value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
