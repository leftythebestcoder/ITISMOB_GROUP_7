package com.itismob.s02.mco2.group7.budgetbuddy.models;

import java.io.Serializable;

public class Budget implements Serializable {
    private String id;
    private String category;
    private double limit;
    private String period;
    private long startDate;
    private long endDate;

    public Budget() {}

    public Budget(String id, String category, double limit, String period,
                  long startDate, long endDate) {
        this.id = id;
        this.category = category;
        this.limit = limit;
        this.period = period;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }
}