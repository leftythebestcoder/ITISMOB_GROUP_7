package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.IncomeDetailActivity;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Income;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeViewHolder> {

    private final ArrayList<Income> data;

    public IncomeAdapter(ArrayList<Income> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income income = data.get(position);
        holder.bindData(income);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), IncomeDetailActivity.class);
            intent.putExtra(IncomeDetailActivity.INCOME_KEY, income);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}