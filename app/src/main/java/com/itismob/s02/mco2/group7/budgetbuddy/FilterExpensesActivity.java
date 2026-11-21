package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;

public class FilterExpensesActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final int DATE_ALL = 0;
    public static final int DATE_TODAY = 1;
    public static final int DATE_WEEK = 2;
    public static final int DATE_MONTH = 3;

    private RadioGroup categoryGroup, dateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_expenses);

        categoryGroup = findViewById(R.id.categoryGroup);
        dateGroup = findViewById(R.id.dateGroup);
        TextView backTv = findViewById(R.id.backTv);

        String currentCategory = getIntent().getStringExtra(EXTRA_CATEGORY);
        int currentDate = getIntent().getIntExtra(EXTRA_DATE, DATE_ALL);
        if (currentCategory == null) currentCategory = "All";

        addCategoryRadio("All", currentCategory);
        for (String c : Categories.ALL) {
            addCategoryRadio(c, currentCategory);
        }

        switch (currentDate) {
            case DATE_TODAY: dateGroup.check(R.id.radioToday); break;
            case DATE_WEEK: dateGroup.check(R.id.radioWeek); break;
            case DATE_MONTH: dateGroup.check(R.id.radioMonth); break;
            default: dateGroup.check(R.id.radioAll);
        }

        backTv.setOnClickListener(v -> finish());

        findViewById(R.id.resetBtn).setOnClickListener(v -> {
            ((RadioButton) categoryGroup.getChildAt(0)).setChecked(true);
            dateGroup.check(R.id.radioAll);
        });

        findViewById(R.id.applyBtn).setOnClickListener(v -> apply());
    }

    private void addCategoryRadio(String label, String selected) {
        MaterialRadioButton rb = new MaterialRadioButton(this);
        rb.setId(View.generateViewId());
        rb.setText(label.equals("All") ? "All" : Categories.icon(label) + "  " + label);
        rb.setTag(label);
        rb.setLayoutParams(new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT));
        categoryGroup.addView(rb);
        if (label.equals(selected)) rb.setChecked(true);
    }

    private void apply() {
        String category = "All";
        View checked = categoryGroup.findViewById(categoryGroup.getCheckedRadioButtonId());
        if (checked != null && checked.getTag() != null) {
            category = checked.getTag().toString();
        }

        int date = DATE_ALL;
        int dateId = dateGroup.getCheckedRadioButtonId();
        if (dateId == R.id.radioToday) date = DATE_TODAY;
        else if (dateId == R.id.radioWeek) date = DATE_WEEK;
        else if (dateId == R.id.radioMonth) date = DATE_MONTH;

        Intent result = new Intent();
        result.putExtra(EXTRA_CATEGORY, category);
        result.putExtra(EXTRA_DATE, date);
        setResult(RESULT_OK, result);
        finish();
    }
}