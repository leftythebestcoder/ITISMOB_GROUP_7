package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Income;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

public class IncomeViewHolder extends RecyclerView.ViewHolder {

    private final TextView iconTv, sourceTv, dateTv, amountTv;

    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);
        iconTv = itemView.findViewById(R.id.iconTv);
        sourceTv = itemView.findViewById(R.id.sourceTv);
        dateTv = itemView.findViewById(R.id.dateTv);
        amountTv = itemView.findViewById(R.id.amountTv);
    }

    public void bindData(Income income) {
        iconTv.setText(Categories.incomeIcon(income.getSource()));
        sourceTv.setText(income.getSource());
        dateTv.setText(Fmt.date(income.getDate()));
        amountTv.setText("+" + Fmt.peso(income.getAmount()));
    }
}