package com.itismob.s02.mco2.group7.budgetbuddy.models;

public class AlertItem {

    public static final int TYPE_EXCEEDED = 0;
    public static final int TYPE_WARNING = 1;
    public static final int TYPE_PAYMENT = 2;

    private final int type;
    private final String title;
    private final String message;
    private final String key;

    public AlertItem(int type, String title, String message, String key) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.key = key;
    }

    public int getType() { return type; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getKey() { return key; }
}