
package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Budget;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Expense;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Income;
import com.itismob.s02.mco2.group7.budgetbuddy.models.Payment;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.BudgetCalc;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Fmt;
import com.itismob.s02.mco2.group7.budgetbuddy.utils.Periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final long DAY = 24L * 60 * 60 * 1000;

    private TextView greetingTv, bellTv, balanceTv, incomeTv, expenseTv,
            budgetDetailTv, budgetPercentTv, logoutTv;
    private CircularProgressIndicator budgetProgress;
    private LinearProgressIndicator incomeBar, expenseBar;
    private final View[] bars = new View[7];

    private final List<Income> incomes = new ArrayList<>();
    private final List<Expense> expenses = new ArrayList<>();
    private final List<Budget> budgets = new ArrayList<>();
    private TextView upcomingTv, addPaymentTv;
    private final List<Payment> payments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        greetingTv = v.findViewById(R.id.greetingTv);
        bellTv = v.findViewById(R.id.bellTv);
        balanceTv = v.findViewById(R.id.balanceTv);
        incomeTv = v.findViewById(R.id.incomeTv);
        expenseTv = v.findViewById(R.id.expenseTv);
        budgetDetailTv = v.findViewById(R.id.budgetDetailTv);
        budgetPercentTv = v.findViewById(R.id.budgetPercentTv);
        budgetProgress = v.findViewById(R.id.budgetProgress);
        incomeBar = v.findViewById(R.id.incomeBar);
        expenseBar = v.findViewById(R.id.expenseBar);
        logoutTv = v.findViewById(R.id.logoutTv);

        int[] barIds = {R.id.bar0, R.id.bar1, R.id.bar2, R.id.bar3,
                R.id.bar4, R.id.bar5, R.id.bar6};

        for (int i = 0; i < 7; i++) {
            bars[i] = v.findViewById(barIds[i]);
        }

        String name = PrefsHelper.getName(requireContext());
        String first = name.isEmpty() ? "there" : name.split(" ")[0];
        greetingTv.setText("Hi, " + first + " 👋");

        bellTv.setOnClickListener(x ->
                ((MainActivity) requireActivity()).selectTab(R.id.nav_alerts));

        View.OnClickListener openSummary = x ->
                startActivity(new Intent(requireContext(), SummaryActivity.class));

        v.findViewById(R.id.summaryCard).setOnClickListener(openSummary);
        v.findViewById(R.id.seeAllTv).setOnClickListener(openSummary);

        upcomingTv = v.findViewById(R.id.upcomingTv);
        addPaymentTv = v.findViewById(R.id.addPaymentTv);

        View.OnClickListener openAddPayment = x ->
                startActivity(new Intent(requireContext(), AddPaymentActivity.class));

        addPaymentTv.setOnClickListener(openAddPayment);
        v.findViewById(R.id.upcomingCard).setOnClickListener(openAddPayment);

        logoutTv.setOnClickListener(x -> {
            PrefsHelper.saveName(requireContext(), "");
            PrefsHelper.saveEmail(requireContext(), "");

            Intent i = new Intent(requireContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        refreshUi();
        showUpcoming();
    }

    private void refreshUi() {
        long monthStart = Periods.monthStart();
        long monthEnd = Periods.monthEnd();

        double totalIncome = 0, totalExpense = 0, monthIncome = 0, monthExpense = 0;

        for (Income i : incomes) {
            totalIncome += i.getAmount();
            if (i.getDate() >= monthStart && i.getDate() <= monthEnd) {
                monthIncome += i.getAmount();
            }
        }

        for (Expense e : expenses) {
            totalExpense += e.getAmount();
            if (e.getDate() >= monthStart && e.getDate() <= monthEnd) {
                monthExpense += e.getAmount();
            }
        }

        balanceTv.setText(Fmt.peso(totalIncome - totalExpense));
        incomeTv.setText(Fmt.peso(monthIncome));
        expenseTv.setText(Fmt.peso(monthExpense));

        long now = System.currentTimeMillis();
        double limit = 0, spent = 0;

        for (Budget b : budgets) {
            if (now < b.getStartDate() || now > b.getEndDate()) continue;
            limit += b.getLimit();
            spent += BudgetCalc.spent(b, expenses);
        }

        if (limit == 0) {
            budgetDetailTv.setText("No active budgets yet");
            budgetPercentTv.setText("0%");
            budgetProgress.setProgress(0);
        } else {
            int pct = BudgetCalc.percent(spent, limit);
            budgetDetailTv.setText(Fmt.peso(spent) + " of " + Fmt.peso(limit) + "\n"
                    + Fmt.peso(Math.max(limit - spent, 0)) + " left");
            budgetPercentTv.setText(pct + "%");
            budgetProgress.setProgress(Math.min(pct, 100));
            budgetProgress.setIndicatorColor(requireContext().getColor(
                    pct >= 100 ? R.color.red_expense : R.color.green_primary));
        }

        double[] days = new double[7];
        long weekStart = Periods.weekStart();

        for (Expense e : expenses) {
            int idx = (int) ((e.getDate() - weekStart) / DAY);
            if (idx >= 0 && idx < 7) {
                days[idx] += e.getAmount();
            }
        }

        double maxDay = 0;
        for (double d : days) maxDay = Math.max(maxDay, d);

        for (int i = 0; i < 7; i++) {
            int heightDp = maxDay == 0 ? 4 : 4 + (int) (80 * days[i] / maxDay);
            LinearLayout.LayoutParams lp =
                    (LinearLayout.LayoutParams) bars[i].getLayoutParams();
            lp.height = dp(heightDp);
            bars[i].setLayoutParams(lp);
        }

        double max = Math.max(monthIncome, monthExpense);
        incomeBar.setProgress(max == 0 ? 0 : (int) (monthIncome / max * 100));
        expenseBar.setProgress(max == 0 ? 0 : (int) (monthExpense / max * 100));
    }

    private void showUpcoming() {
        long today = Periods.startOfDay(System.currentTimeMillis());

        ArrayList<Payment> next = new ArrayList<>();

        for (Payment p : payments) {
            if (p.getDueDate() >= today) next.add(p);
        }

        Collections.sort(next,
                (a, b) -> Long.compare(a.getDueDate(), b.getDueDate()));

        if (next.isEmpty()) {
            upcomingTv.setText("No upcoming payments");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Math.min(next.size(), 3); i++) {
            Payment p = next.get(i);

            if (i > 0) sb.append("\n\n");

            sb.append("🎓 ").append(p.getName()).append(" · ")
                    .append(Fmt.peso(p.getAmount()))
                    .append("\nDue ").append(Fmt.dueIn(p.getDueDate()))
                    .append(" · ").append(Fmt.date(p.getDueDate()))
                    .append(" ").append(Fmt.time(p.getDueDate()));
        }

        upcomingTv.setText(sb.toString());
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
