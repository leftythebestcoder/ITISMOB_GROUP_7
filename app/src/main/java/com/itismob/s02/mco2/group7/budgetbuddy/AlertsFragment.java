
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.adapters.AlertAdapter;
import com.itismob.s02.mco2.group7.budgetbuddy.models.AlertItem;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Payment;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.AlertBuilder;

import java.util.ArrayList;

public class AlertsFragment extends Fragment {

    private final ArrayList<Budget> budgets = new ArrayList<>();
    private final ArrayList<Expense> expenses = new ArrayList<>();
    private final ArrayList<Payment> payments = new ArrayList<>();
    private final ArrayList<AlertItem> shown = new ArrayList<>();

    private AlertAdapter adapter;
    private TextView emptyTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        emptyTv = v.findViewById(R.id.emptyTv);
        RecyclerView rv = v.findViewById(R.id.alertRv);

        adapter = new AlertAdapter(shown);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        refresh();
    }

    private void refresh() {
        shown.clear();
        shown.addAll(AlertBuilder.build(budgets, expenses, payments));

        adapter.notifyDataSetChanged();

        emptyTv.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
