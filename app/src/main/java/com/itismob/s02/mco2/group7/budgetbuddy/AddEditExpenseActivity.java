
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.Calendar;

public class AddEditExpenseActivity extends AppCompatActivity {

    private TextInputEditText amountEt, dateEt, notesEt;
    private MaterialAutoCompleteTextView categoryDd;
    private TextView titleTv, backTv;
    private MaterialButton saveBtn;

    private long selectedDate = Periods.startOfDay(System.currentTimeMillis());
    private Expense editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        titleTv = findViewById(R.id.titleTv);
        backTv = findViewById(R.id.backTv);
        amountEt = findViewById(R.id.amountEt);
        categoryDd = findViewById(R.id.categoryDd);
        dateEt = findViewById(R.id.dateEt);
        notesEt = findViewById(R.id.notesEt);
        saveBtn = findViewById(R.id.saveBtn);

        categoryDd.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, Categories.ALL));
        categoryDd.setText(Categories.ALL[0], false);

        editing = (Expense) getIntent().getSerializableExtra(
                ExpenseDetailActivity.EXPENSE_KEY);

        if (editing != null) {
            titleTv.setText(R.string.title_edit_expense);
            amountEt.setText(formatAmount(editing.getAmount()));
            categoryDd.setText(editing.getCategory(), false);
            notesEt.setText(editing.getNotes());
            selectedDate = editing.getDate();
        }

        dateEt.setText(Fmt.date(selectedDate));

        backTv.setOnClickListener(v -> finish());
        dateEt.setOnClickListener(v -> showDatePicker());
        saveBtn.setOnClickListener(v -> save());
    }

    private String formatAmount(double a) {
        return a == Math.floor(a)
                ? String.valueOf((long) a)
                : String.valueOf(a);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(selectedDate);

        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, month, day);

            selectedDate = Periods.startOfDay(picked.getTimeInMillis());
            dateEt.setText(Fmt.date(selectedDate));

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void save() {
        double amount;

        try {
            amount = Double.parseDouble(amountEt.getText().toString().trim());
        } catch (NumberFormatException e) {
            amountEt.setError("Enter a valid amount");
            return;
        }

        if (amount <= 0) {
            amountEt.setError("Amount must be more than 0");
            return;
        }

        String category = categoryDd.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(this, "Choose a category", Toast.LENGTH_SHORT).show();
            return;
        }

        String notes = notesEt.getText().toString().trim();

        if (editing == null) {
            Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
