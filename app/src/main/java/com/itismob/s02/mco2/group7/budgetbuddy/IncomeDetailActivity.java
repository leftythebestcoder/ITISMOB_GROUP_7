package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Income;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

public class IncomeDetailActivity extends AppCompatActivity {

    public static final String INCOME_KEY = "INCOME_KEY";

    private TextView amountTv, dateTv, sourceTv, notesTv, backTv;
    private MaterialButton editBtn, deleteBtn;
    private Income current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_detail);

        amountTv = findViewById(R.id.amountTv);
        dateTv = findViewById(R.id.dateTv);
        sourceTv = findViewById(R.id.sourceTv);
        notesTv = findViewById(R.id.notesTv);
        backTv = findViewById(R.id.backTv);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        current = (Income) getIntent().getSerializableExtra(INCOME_KEY);

        if (current == null) {
            finish();
            return;
        }

        show(current);

        backTv.setOnClickListener(v -> finish());

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditIncomeActivity.class);
            intent.putExtra(INCOME_KEY, current);
            startActivity(intent);
        });

        deleteBtn.setOnClickListener(v -> confirmDelete());
    }

    private void show(Income income) {
        amountTv.setText(Fmt.peso(income.getAmount()));
        dateTv.setText(Fmt.date(income.getDate()));
        sourceTv.setText(income.getSource());

        String notes = income.getNotes();
        notesTv.setText(notes == null || notes.isEmpty() ? "No notes" : notes);
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete income?")
                .setMessage("This will permanently remove this income record.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> {
                    Toast.makeText(this, "Income deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .show();
    }
}