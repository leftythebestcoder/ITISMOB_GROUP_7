package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.BudgetCalc;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

public class BudgetViewHolder extends RecyclerView.ViewHolder {

    private final TextView iconTv, categoryTv, amountTv, statusTv, rangeTv;
    private final LinearProgressIndicator progressBar;

    public BudgetViewHolder(@NonNull View itemView) {
        super(itemView);
        iconTv = itemView.findViewById(R.id.iconTv);
        categoryTv = itemView.findViewById(R.id.categoryTv);
        amountTv = itemView.findViewById(R.id.amountTv);
        statusTv = itemView.findViewById(R.id.statusTv);
        rangeTv = itemView.findViewById(R.id.rangeTv);
        progressBar = itemView.findViewById(R.id.progressBar);
    }

    public void bindData(Budget budget, double spent) {
        int pct = BudgetCalc.percent(spent, budget.getLimit());

        iconTv.setText(Categories.icon(budget.getCategory()));
        categoryTv.setText(budget.getCategory());
        amountTv.setText(Fmt.peso(spent) + " / " + Fmt.peso(budget.getLimit()));

        progressBar.setProgress(Math.min(pct, 100));
        progressBar.setIndicatorColor(BudgetCalc.color(itemView.getContext(), pct));

        String status = pct + "% used";
        if (pct >= 100) status += " · ⚠ limit reached";
        else if (pct >= 70) status += " · nearing limit";
        statusTv.setText(status);

        rangeTv.setText(Fmt.date(budget.getStartDate()) + " – " + Fmt.date(budget.getEndDate()));
    }
}
