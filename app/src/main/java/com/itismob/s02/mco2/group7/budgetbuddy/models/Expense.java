package com.itismob.s02.mco2.group7.budgetbuddy.models;

import java.io.Serializable;

public class Expense implements Serializable {
    private String id;
    private double amount;
    private String category;
    private String notes;
    private long date;

    public Expense() {}

    public Expense(String id, double amount, String category, String notes, long date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.notes = notes;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}