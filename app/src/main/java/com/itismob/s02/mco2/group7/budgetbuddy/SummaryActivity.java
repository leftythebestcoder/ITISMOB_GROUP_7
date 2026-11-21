
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.DonutChartView;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    private static final long DAY = 24L * 60 * 60 * 1000;

    private final ArrayList<Expense> expenses = new ArrayList<>();

    private DonutChartView donut;
    private TextView totalTv, emptyTv;
    private LinearLayout legendList;
    private String period = "Month";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        donut = findViewById(R.id.donut);
        totalTv = findViewById(R.id.totalTv);
        emptyTv = findViewById(R.id.emptyTv);
        legendList = findViewById(R.id.legendList);
        MaterialButtonToggleGroup periodGroup = findViewById(R.id.periodGroup);

        findViewById(R.id.backTv).setOnClickListener(v -> finish());

        periodGroup.check(R.id.monthBtn);
        periodGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.weekBtn) {
                period = "Week";
            } else if (checkedId == R.id.yearBtn) {
                period = "Year";
            } else {
                period = "Month";
            }

            refresh();
        });

        refresh();
    }

    private void refresh() {
        long from, to;

        if (period.equals("Week")) {
            from = Periods.weekStart();
            to = from + 7 * DAY - 1;
        } else if (period.equals("Year")) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_YEAR, 1);
            from = Periods.startOfDay(c.getTimeInMillis());

            c.set(Calendar.MONTH, Calendar.DECEMBER);
            c.set(Calendar.DAY_OF_MONTH, 31);
            to = Periods.endOfDay(c.getTimeInMillis());
        } else {
            from = Periods.monthStart();
            to = Periods.monthEnd();
        }

        LinkedHashMap<String, Double> totals = new LinkedHashMap<>();
        double total = 0;

        for (Expense e : expenses) {
            if (e.getDate() < from || e.getDate() > to) continue;

            String cat = e.getCategory() == null ? "Other" : e.getCategory();
            Double old = totals.get(cat);
            totals.put(cat, (old == null ? 0 : old) + e.getAmount());
            total += e.getAmount();
        }

        ArrayList<Map.Entry<String, Double>> rows =
                new ArrayList<>(totals.entrySet());

        Collections.sort(rows,
                (a, b) -> Double.compare(b.getValue(), a.getValue()));

        float[] values = new float[rows.size()];
        int[] colors = new int[rows.size()];

        for (int i = 0; i < rows.size(); i++) {
            values[i] = rows.get(i).getValue().floatValue();
            colors[i] = Categories.color(rows.get(i).getKey());
        }

        donut.setData(values, colors);
        totalTv.setText(Fmt.peso(total));

        legendList.removeAllViews();
        emptyTv.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < rows.size(); i++) {
            String cat = rows.get(i).getKey();
            double amount = rows.get(i).getValue();
            int pct = total > 0
                    ? (int) Math.round(amount / total * 100)
                    : 0;

            View row = inflater.inflate(R.layout.item_legend, legendList, false);

            row.findViewById(R.id.dotView)
                    .setBackgroundTintList(ColorStateList.valueOf(colors[i]));

            ((TextView) row.findViewById(R.id.nameTv))
                    .setText(Categories.icon(cat) + "  " + cat);

            ((TextView) row.findViewById(R.id.percentTv))
                    .setText(pct + "%");

            ((TextView) row.findViewById(R.id.amountTv))
                    .setText(Fmt.peso(amount));

            legendList.addView(row);
        }
    }
}
