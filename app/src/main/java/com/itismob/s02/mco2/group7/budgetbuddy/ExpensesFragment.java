
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itismob.s02.mco2.group7.budgetbuddy.adapters.ExpenseAdapter;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.ArrayList;
import java.util.Collections;

public class ExpensesFragment extends Fragment {

    private static final long DAY = 24L * 60 * 60 * 1000;

    private final ArrayList<Expense> all = new ArrayList<>();
    private final ArrayList<Expense> shown = new ArrayList<>();

    private ExpenseAdapter adapter;
    private TextView monthTotalTv, emptyTv, filterInfoTv;

    private String categoryFilter = "All";
    private int dateFilter = FilterExpensesActivity.DATE_ALL;

    private final ActivityResultLauncher<Intent> filterLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), result -> {
                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null) {

                            Intent data = result.getData();

                            categoryFilter = data.getStringExtra(
                                    FilterExpensesActivity.EXTRA_CATEGORY);

                            dateFilter = data.getIntExtra(
                                    FilterExpensesActivity.EXTRA_DATE,
                                    FilterExpensesActivity.DATE_ALL);

                            if (categoryFilter == null) categoryFilter = "All";

                            applyFilter();
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        monthTotalTv = v.findViewById(R.id.monthTotalTv);
        emptyTv = v.findViewById(R.id.emptyTv);
        filterInfoTv = v.findViewById(R.id.filterInfoTv);

        RecyclerView rv = v.findViewById(R.id.expenseRv);

        adapter = new ExpenseAdapter(shown);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        v.findViewById(R.id.addExpenseBtn).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), AddEditExpenseActivity.class)));

        v.findViewById(R.id.summaryBtn).setOnClickListener(x ->
                startActivity(new Intent(requireContext(), SummaryActivity.class)));

        v.findViewById(R.id.filterBtn).setOnClickListener(x -> {
            Intent i = new Intent(requireContext(), FilterExpensesActivity.class);
            i.putExtra(FilterExpensesActivity.EXTRA_CATEGORY, categoryFilter);
            i.putExtra(FilterExpensesActivity.EXTRA_DATE, dateFilter);
            filterLauncher.launch(i);
        });

        applyFilter();
    }

    private void applyFilter() {
        long from = 0;
        long to = Long.MAX_VALUE;
        String dateLabel = "";
        long now = System.currentTimeMillis();

        switch (dateFilter) {
            case FilterExpensesActivity.DATE_TODAY:
                from = Periods.startOfDay(now);
                to = Periods.endOfDay(now);
                dateLabel = "Today";
                break;

            case FilterExpensesActivity.DATE_WEEK:
                from = Periods.weekStart();
                to = from + 7 * DAY - 1;
                dateLabel = "This week";
                break;

            case FilterExpensesActivity.DATE_MONTH:
                from = Periods.monthStart();
                to = Periods.monthEnd();
                dateLabel = "This month";
                break;
        }

        shown.clear();
        double shownTotal = 0;

        for (Expense e : all) {
            boolean categoryOk = categoryFilter.equals("All")
                    || categoryFilter.equals(e.getCategory());

            boolean dateOk = e.getDate() >= from && e.getDate() <= to;

            if (categoryOk && dateOk) {
                shown.add(e);
                shownTotal += e.getAmount();
            }
        }

        Collections.sort(shown,
                (a, b) -> Long.compare(b.getDate(), a.getDate()));

        adapter.notifyDataSetChanged();

        emptyTv.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);

        boolean filtering = !categoryFilter.equals("All")
                || dateFilter != FilterExpensesActivity.DATE_ALL;

        if (filtering) {
            String label = categoryFilter.equals("All") ? "" : categoryFilter;

            if (!dateLabel.isEmpty()) {
                label += (label.isEmpty() ? "" : " · ") + dateLabel;
            }

            filterInfoTv.setText("Filter: " + label + " · " + Fmt.peso(shownTotal));
        } else {
            filterInfoTv.setText("Sorted: Newest first");
        }

        long mStart = Periods.monthStart();
        long mEnd = Periods.monthEnd();
        double monthTotal = 0;

        for (Expense e : all) {
            if (e.getDate() >= mStart && e.getDate() <= mEnd) {
                monthTotal += e.getAmount();
            }
        }

        monthTotalTv.setText(Fmt.peso(monthTotal));
    }
}
