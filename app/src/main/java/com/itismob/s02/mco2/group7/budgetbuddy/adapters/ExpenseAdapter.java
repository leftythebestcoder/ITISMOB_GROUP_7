package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.ExpenseDetailActivity;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private final ArrayList<Expense> data;

    public ExpenseAdapter(ArrayList<Expense> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = data.get(position);
        holder.bindData(expense);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ExpenseDetailActivity.class);
            intent.putExtra(ExpenseDetailActivity.EXPENSE_KEY, expense);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
