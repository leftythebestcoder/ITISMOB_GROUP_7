package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;

import java.util.List;

import android.content.Context;
import com.itismob.s02.mco2.group7.budgetbuddy.R;

public class BudgetCalc {

    public static double spent(Budget b, List<Expense> expenses) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getCategory() != null
                    && e.getCategory().equals(b.getCategory())
                    && e.getDate() >= b.getStartDate()
                    && e.getDate() <= b.getEndDate()) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public static int percent(double spent, double limit) {
        if (limit <= 0) return 0;
        return (int) Math.round(spent / limit * 100);
    }

    public static int color(Context ctx, int percent) {
        int res = percent >= 100 ? R.color.red_expense
                : percent >= 70 ? R.color.orange_warn
                  : R.color.green_primary;
        return ctx.getColor(res);
    }
}