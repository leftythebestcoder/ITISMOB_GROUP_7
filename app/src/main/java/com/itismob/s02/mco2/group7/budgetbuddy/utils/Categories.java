package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import android.graphics.Color;

public class Categories {

    public static final String[] ALL = {
            "Food", "Transport", "School", "Shopping", "Entertainment", "Other"
    };

    public static final String[] INCOME_SOURCES = {
            "Allowance", "Part-time Job", "Scholarship", "Other"
    };

    public static String icon(String category) {
        if (category == null) return "⋯";
        switch (category) {
            case "Food": return "🍴";
            case "Transport": return "🚌";
            case "School": return "🎓";
            case "Shopping": return "🛍";
            case "Entertainment": return "🎬";
            default: return "⋯";
        }
    }

    public static int color(String category) {
        if (category == null) return Color.parseColor("#8B5CF6");
        switch (category) {
            case "Food": return Color.parseColor("#1F9D6B");
            case "Transport": return Color.parseColor("#3B82F6");
            case "School": return Color.parseColor("#F5A623");
            case "Shopping": return Color.parseColor("#E5484D");
            case "Entertainment": return Color.parseColor("#EC4899");
            default: return Color.parseColor("#8B5CF6");
        }
    }

    public static String incomeIcon(String source) {
        if (source == null) return "💰";
        switch (source) {
            case "Allowance": return "💼";
            case "Part-time Job": return "🧾";
            case "Scholarship": return "🎓";
            default: return "💰";
        }
    }
}