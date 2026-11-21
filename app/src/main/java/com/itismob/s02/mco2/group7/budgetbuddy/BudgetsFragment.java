
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
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

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.itismob.s02.mco2.group7.budgetbuddy.adapters.BudgetAdapter;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.BudgetCalc;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

import java.util.ArrayList;
import java.util.Collections;

public class BudgetsFragment extends Fragment {

    private final ArrayList<Budget> all = new ArrayList<>();
    private final ArrayList<Budget> shown = new ArrayList<>();
    private final ArrayList<Expense> expenses = new ArrayList<>();

    private BudgetAdapter adapter;
    private TextView overallDetailTv, overallPercentTv, emptyTv;
    private CircularProgressIndicator overallProgress;
    private String period = "Monthly";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budgets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        overallDetailTv = v.findViewById(R.id.overallDetailTv);
        overallPercentTv = v.findViewById(R.id.overallPercentTv);
        overallProgress = v.findViewById(R.id.overallProgress);
        emptyTv = v.findViewById(R.id.emptyTv);

        RecyclerView rv = v.findViewById(R.id.budgetRv);
        MaterialButtonToggleGroup periodGroup = v.findViewById(R.id.periodGroup);

        adapter = new BudgetAdapter(shown, expenses);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        periodGroup.check(R.id.periodMonthlyBtn);

        periodGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.periodWeeklyBtn) {
                period = "Weekly";
            } else if (checkedId == R.id.periodDailyBtn) {
                period = "Daily";
            } else {
                period = "Monthly";
            }

            refresh();
        });

        v.findViewById(R.id.newBudgetBtn).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), AddEditBudgetActivity.class)));

        refresh();
    }

    private void refresh() {
        long now = System.currentTimeMillis();

        shown.clear();
        double limit = 0, spent = 0;

        for (Budget b : all) {
            if (!period.equals(b.getPeriod())) continue;

            shown.add(b);

            if (now >= b.getStartDate() && now <= b.getEndDate()) {
                limit += b.getLimit();
                spent += BudgetCalc.spent(b, expenses);
            }
        }

        Collections.sort(shown,
                (a, b) -> a.getCategory().compareTo(b.getCategory()));

        adapter.notifyDataSetChanged();

        emptyTv.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);

        if (limit == 0) {
            overallDetailTv.setText("No active " + period.toLowerCase() + " budgets");
            overallPercentTv.setText("0%");
            overallProgress.setProgress(0);
        } else {
            int pct = BudgetCalc.percent(spent, limit);

            overallDetailTv.setText(Fmt.peso(spent) + " / " + Fmt.peso(limit) + "\n"
                    + Fmt.peso(Math.max(limit - spent, 0)) + " left");

            overallPercentTv.setText(pct + "%");
            overallProgress.setProgress(Math.min(pct, 100));
            overallProgress.setIndicatorColor(BudgetCalc.color(requireContext(), pct));
        }
    }
}
