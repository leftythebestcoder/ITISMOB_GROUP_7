
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Categories;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

public class ExpenseDetailActivity extends AppCompatActivity {

    public static final String EXPENSE_KEY = "EXPENSE_KEY";

    private TextView iconTv, amountTv, dateTv, categoryTv, notesTv, backTv;
    private MaterialButton editBtn, deleteBtn;
    private Expense current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        iconTv = findViewById(R.id.iconTv);
        amountTv = findViewById(R.id.amountTv);
        dateTv = findViewById(R.id.dateTv);
        categoryTv = findViewById(R.id.categoryTv);
        notesTv = findViewById(R.id.notesTv);
        backTv = findViewById(R.id.backTv);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        current = (Expense) getIntent().getSerializableExtra(EXPENSE_KEY);

        if (current == null) {
            finish();
            return;
        }

        show(current);

        backTv.setOnClickListener(v -> finish());

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
            intent.putExtra(EXPENSE_KEY, current);
            startActivity(intent);
        });

        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private void show(Expense e) {
        iconTv.setText(Categories.icon(e.getCategory()));
        amountTv.setText(Fmt.peso(e.getAmount()));
        dateTv.setText(Fmt.date(e.getDate()));
        categoryTv.setText(e.getCategory());

        String notes = e.getNotes();
        notesTv.setText(notes == null || notes.isEmpty() ? "No notes" : notes);
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete expense?")
                .setMessage("This will permanently remove this expense record.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> {
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }
}
