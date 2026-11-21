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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itismob.s02.mco2.group7.budgetbuddy.adapters.IncomeAdapter;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Income;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.ArrayList;
import java.util.Collections;

public class IncomeFragment extends Fragment {

    private final ArrayList<Income> all = new ArrayList<>();
    private final ArrayList<Income> shown = new ArrayList<>();

    private IncomeAdapter adapter;
    private TextView monthTotalTv, emptyTv;
    private String filter = "All";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        monthTotalTv = v.findViewById(R.id.monthTotalTv);
        emptyTv = v.findViewById(R.id.emptyTv);
        RecyclerView rv = v.findViewById(R.id.incomeRv);
        ChipGroup chipGroup = v.findViewById(R.id.chipGroup);
        MaterialButton addBtn = v.findViewById(R.id.addIncomeBtn);

        adapter = new IncomeAdapter(shown);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;

            Chip chip = group.findViewById(checkedIds.get(0));
            filter = chip.getText().toString();
            applyFilter();
        });

        addBtn.setOnClickListener(x ->
                startActivity(new Intent(requireContext(), AddEditIncomeActivity.class)));

        applyFilter();
    }

    private void applyFilter() {
        shown.clear();

        for (Income i : all) {
            if (filter.equals("All") || filter.equals(i.getSource())) {
                shown.add(i);
            }
        }

        Collections.sort(shown,
                (a, b) -> Long.compare(b.getDate(), a.getDate()));

        adapter.notifyDataSetChanged();

        emptyTv.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);

        long start = Periods.monthStart();
        long end = Periods.monthEnd();
        double total = 0;

        for (Income i : all) {
            if (i.getDate() >= start && i.getDate() <= end) {
                total += i.getAmount();
            }
        }

        monthTotalTv.setText(Fmt.peso(total));
    }
}