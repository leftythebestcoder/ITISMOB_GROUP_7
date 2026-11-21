package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fmt {

    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat DATE =
            new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

    public static String peso(double amount) {
        return "₱" + MONEY.format(amount);
    }

    public static String date(long millis) {
        return DATE.format(new Date(millis));
    }

    private static final SimpleDateFormat TIME =
            new SimpleDateFormat("h:mm a", Locale.getDefault());

    public static String time(long millis) {
        return TIME.format(new Date(millis));
    }

    public static String dueIn(long dueMillis) {
        long day = 24L * 60 * 60 * 1000;
        long days = Math.round(
                (Periods.startOfDay(dueMillis) - Periods.startOfDay(System.currentTimeMillis()))
                        / (double) day);
        if (days < 0) return "overdue";
        if (days == 0) return "today";
        if (days == 1) return "tomorrow";
        return "in " + days + " days";
    }

}