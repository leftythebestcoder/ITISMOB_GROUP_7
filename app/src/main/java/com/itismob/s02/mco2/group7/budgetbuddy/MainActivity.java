
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_OPEN_ALERTS = "EXTRA_OPEN_ALERTS";

    private BottomNavigationView bottomNav;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);
        addFab = findViewById(R.id.addFab);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment f;

            if (id == R.id.nav_home) {
                f = new HomeFragment();
            } else if (id == R.id.nav_transactions) {
                f = new TransactionsFragment();
            } else if (id == R.id.nav_budgets) {
                f = new BudgetsFragment();
            } else {
                f = new AlertsFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, f)
                    .commit();

            return true;
        });

        if (savedInstanceState == null) {
            boolean openAlerts = getIntent().getBooleanExtra(EXTRA_OPEN_ALERTS, false);
            bottomNav.setSelectedItemId(
                    openAlerts ? R.id.nav_alerts : R.id.nav_home
            );
        }

        addFab.setOnClickListener(v -> showAddDialog());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (intent.getBooleanExtra(EXTRA_OPEN_ALERTS, false)) {
            bottomNav.setSelectedItemId(R.id.nav_alerts);
        }
    }

    public void selectTab(int menuId) {
        bottomNav.setSelectedItemId(menuId);
    }

    private void showAddDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Add new")
                .setItems(new String[]{"Add Income", "Add Expense"}, (dialog, which) -> {
                    if (which == 0) {
                        startActivity(new Intent(this, AddEditIncomeActivity.class));
                    } else {
                        startActivity(new Intent(this, AddEditExpenseActivity.class));
                    }
                })
                .show();
    }
}
