package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import com.itismob.s02.mco2.group7.budgetbuddy.models.AlertItem;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlertBuilder {

    private static final long DAY = 24L * 60 * 60 * 1000;

    public static List<AlertItem> build(List<Budget> budgets, List<Expense> expenses,
                                        List<Payment> payments) {
        List<AlertItem> out = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Budget b : budgets) {
            if (now < b.getStartDate() || now > b.getEndDate()) continue;

            int pct = BudgetCalc.percent(BudgetCalc.spent(b, expenses), b.getLimit());
            String cat = b.getCategory();
            String suffix = b.getId() + "_" + b.getStartDate();

            if (pct >= 100) {
                int over = pct - 100;
                String msg = over == 0
                        ? cat + " budget limit reached."
                        : cat + " budget exceeded by " + over + "%.";
                out.add(new AlertItem(AlertItem.TYPE_EXCEEDED, "Budget Exceeded", msg,
                        "over_" + suffix));
            } else if (pct >= 80) {
                out.add(new AlertItem(AlertItem.TYPE_WARNING, "Budget Alert",
                        "You've used " + pct + "% of your " + cat + " budget.",
                        "warn_" + suffix));
            }
        }

        long today = Periods.startOfDay(now);
        for (Payment p : payments) {
            long days = Math.round((Periods.startOfDay(p.getDueDate()) - today) / (double) DAY);
            if (days < 0 || days > 7) continue;

            String when = days == 0 ? "today" : days == 1 ? "tomorrow" : "in " + days + " days";
            out.add(new AlertItem(AlertItem.TYPE_PAYMENT, "Upcoming Payment",
                    p.getName() + " (" + Fmt.peso(p.getAmount()) + ") is due " + when + ".",
                    "pay_" + p.getId()));
        }

        Collections.sort(out, (a, b) -> Integer.compare(a.getType(), b.getType()));
        return out;
    }
}