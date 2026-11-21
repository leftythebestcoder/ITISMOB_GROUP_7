
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.Calendar;

public class AddEditBudgetActivity extends AppCompatActivity {

    public static final String BUDGET_KEY = "BUDGET_KEY";

    private TextView titleTv, backTv;
    private MaterialButtonToggleGroup periodGroup;
    private MaterialAutoCompleteTextView categoryDd;
    private TextInputEditText limitEt, startEt, endEt;
    private MaterialButton saveBtn, deleteBtn;

    private Budget editing;
    private String period = "Monthly";
    private long startDate = Periods.monthStart();
    private long endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_budget);

        titleTv = findViewById(R.id.titleTv);
        backTv = findViewById(R.id.backTv);
        periodGroup = findViewById(R.id.periodGroup);
        categoryDd = findViewById(R.id.categoryDd);
        limitEt = findViewById(R.id.limitEt);
        startEt = findViewById(R.id.startEt);
        endEt = findViewById(R.id.endEt);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        categoryDd.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Categories.ALL));
        categoryDd.setText(Categories.ALL[0], false);

        editing = (Budget) getIntent().getSerializableExtra(BUDGET_KEY);

        if (editing != null) {
            titleTv.setText(R.string.title_edit_budget);
            categoryDd.setText(editing.getCategory(), false);
            limitEt.setText(formatAmount(editing.getLimit()));
            period = editing.getPeriod();
            startDate = editing.getStartDate();
            deleteBtn.setVisibility(android.view.View.VISIBLE);
        }

        periodGroup.check(idForPeriod(period));
        endDate = computeEnd(period, startDate);
        refreshDates();

        periodGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            period = periodForId(checkedId);

            if (editing == null) {
                startDate = defaultStart(period);
            }

            endDate = computeEnd(period, startDate);
            refreshDates();
        });

        backTv.setOnClickListener(v -> finish());
        startEt.setOnClickListener(v -> showStartPicker());
        saveBtn.setOnClickListener(v -> save());
        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private int idForPeriod(String p) {
        if ("Weekly".equals(p)) return R.id.periodWeeklyBtn;
        if ("Daily".equals(p)) return R.id.periodDailyBtn;
        return R.id.periodMonthlyBtn;
    }

    private String periodForId(int id) {
        if (id == R.id.periodWeeklyBtn) return "Weekly";
        if (id == R.id.periodDailyBtn) return "Daily";
        return "Monthly";
    }

    private long defaultStart(String p) {
        if ("Weekly".equals(p)) return Periods.weekStart();
        if ("Daily".equals(p)) {
            return Periods.startOfDay(System.currentTimeMillis());
        }
        return Periods.monthStart();
    }

    private long computeEnd(String p, long start) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(start);

        if ("Weekly".equals(p)) {
            c.add(Calendar.DAY_OF_MONTH, 6);
        } else if ("Monthly".equals(p)) {
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DAY_OF_MONTH, -1);
        }

        return Periods.endOfDay(c.getTimeInMillis());
    }

    private void refreshDates() {
        startEt.setText(Fmt.date(startDate));
        endEt.setText(Fmt.date(endDate));
    }

    private void showStartPicker() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startDate);

        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, month, day);

            startDate = Periods.startOfDay(picked.getTimeInMillis());
            endDate = computeEnd(period, startDate);
            refreshDates();

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String formatAmount(double a) {
        return a == Math.floor(a)
                ? String.valueOf((long) a)
                : String.valueOf(a);
    }

    private void save() {
        double limit;

        try {
            limit = Double.parseDouble(limitEt.getText().toString().trim());
        } catch (NumberFormatException e) {
            limitEt.setError("Enter a valid limit");
            return;
        }

        if (limit <= 0) {
            limitEt.setError("Limit must be more than 0");
            return;
        }

        String category = categoryDd.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(this, "Choose a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editing == null) {
            Toast.makeText(this, "Budget created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Budget updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete budget?")
                .setMessage("This removes the budget limit. Your expenses are not deleted.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> {
                    Toast.makeText(this, "Budget deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }
}
