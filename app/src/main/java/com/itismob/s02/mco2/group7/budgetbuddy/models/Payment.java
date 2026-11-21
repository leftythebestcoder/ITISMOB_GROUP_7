package com.itismob.s02.mco2.group7.budgetbuddy.models;

import java.io.Serializable;

public class Payment implements Serializable {
    private String id;
    private String name;
    private double amount;
    private long dueDate;
    private boolean addedToCalendar;

    public Payment() {}

    public Payment(String id, String name, double amount, long dueDate, boolean addedToCalendar) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.addedToCalendar = addedToCalendar;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
    public boolean isAddedToCalendar() { return addedToCalendar; }
    public void setAddedToCalendar(boolean addedToCalendar) { this.addedToCalendar = addedToCalendar; }
}