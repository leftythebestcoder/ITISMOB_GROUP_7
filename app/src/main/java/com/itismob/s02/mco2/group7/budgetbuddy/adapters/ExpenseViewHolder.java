package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {

    private final TextView iconTv, categoryTv, subtitleTv, amountTv;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        iconTv = itemView.findViewById(R.id.iconTv);
        categoryTv = itemView.findViewById(R.id.categoryTv);
        subtitleTv = itemView.findViewById(R.id.subtitleTv);
        amountTv = itemView.findViewById(R.id.amountTv);
    }

    public void bindData(Expense expense) {
        iconTv.setText(Categories.icon(expense.getCategory()));
        categoryTv.setText(expense.getCategory());

        String notes = expense.getNotes();
        String subtitle = (notes == null || notes.isEmpty() ? "" : notes + " · ")
                + Fmt.date(expense.getDate());
        subtitleTv.setText(subtitle);

        amountTv.setText("-" + Fmt.peso(expense.getAmount()));
    }
}