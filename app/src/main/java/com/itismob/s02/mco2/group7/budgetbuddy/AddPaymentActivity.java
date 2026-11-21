
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;

import java.util.Calendar;

public class AddPaymentActivity extends AppCompatActivity {

    private TextInputEditText nameEt, amountEt, dateEt, timeEt;

    private final Calendar due = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        nameEt = findViewById(R.id.nameEt);
        amountEt = findViewById(R.id.amountEt);
        dateEt = findViewById(R.id.dateEt);
        timeEt = findViewById(R.id.timeEt);

        due.add(Calendar.DAY_OF_MONTH, 1);
        due.set(Calendar.HOUR_OF_DAY, 10);
        due.set(Calendar.MINUTE, 0);
        due.set(Calendar.SECOND, 0);
        due.set(Calendar.MILLISECOND, 0);

        refreshFields();

        findViewById(R.id.backTv).setOnClickListener(v -> finish());
        dateEt.setOnClickListener(v -> showDatePicker());
        timeEt.setOnClickListener(v -> showTimePicker());
        findViewById(R.id.calendarBtn).setOnClickListener(v -> save(true));
        findViewById(R.id.saveOnlyBtn).setOnClickListener(v -> save(false));
    }

    private void refreshFields() {
        dateEt.setText(Fmt.date(due.getTimeInMillis()));
        timeEt.setText(Fmt.time(due.getTimeInMillis()));
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            due.set(year, month, day);
            refreshFields();
        }, due.get(Calendar.YEAR), due.get(Calendar.MONTH),
                due.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hour, minute) -> {
            due.set(Calendar.HOUR_OF_DAY, hour);
            due.set(Calendar.MINUTE, minute);
            refreshFields();
        }, due.get(Calendar.HOUR_OF_DAY),
                due.get(Calendar.MINUTE), false).show();
    }

    private void save(boolean addToCalendar) {
        String name = nameEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEt.setError("Enter the payment name");
            return;
        }

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

        long dueMillis = due.getTimeInMillis();

        if (addToCalendar) {
            openCalendar(name, amount, dueMillis);
        } else {
            Toast.makeText(this, "Payment saved", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void openCalendar(String name, double amount, long dueMillis) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "Pay: " + name)
                .putExtra(CalendarContract.Events.DESCRIPTION,
                        "BudgetBuddy reminder: " + Fmt.peso(amount))
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dueMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        dueMillis + 30 * 60 * 1000L);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    "Payment saved, but no calendar app was found",
                    Toast.LENGTH_LONG).show();
        }
    }
}
