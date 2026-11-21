package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.AddEditBudgetActivity;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.BudgetCalc;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetViewHolder> {

    private final ArrayList<Budget> data;
    private final ArrayList<Expense> expenses;   // used to compute "spent"

    public BudgetAdapter(ArrayList<Budget> data, ArrayList<Expense> expenses) {
        this.data = data;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = data.get(position);
        holder.bindData(budget, BudgetCalc.spent(budget, expenses));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddEditBudgetActivity.class);
            intent.putExtra(AddEditBudgetActivity.BUDGET_KEY, budget);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}