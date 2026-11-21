package com.itismob.s02.mco2.group7.budgetbuddy.models;

import java.io.Serializable;

public class Income implements Serializable {
    private String id;
    private double amount;
    private String source;
    private String notes;
    private long date;

    public Income() {}

    public Income(String id, double amount, String source, String notes, long date) {
        this.id = id;
        this.amount = amount;
        this.source = source;
        this.notes = notes;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}