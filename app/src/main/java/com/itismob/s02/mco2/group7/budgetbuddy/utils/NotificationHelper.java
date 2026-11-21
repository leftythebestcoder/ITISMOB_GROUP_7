package com.itismob.s02.mco2.group7.budgetbuddy.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.itismob.s02.mco2.group7.budgetbuddy.MainActivity;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.AlertItem;

public class NotificationHelper {

    private static final String CHANNEL_ID = "budgetbuddy_alerts";

    public static void createChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Budget & payment alerts",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Budget limits and upcoming payments");
            ctx.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingPermission")
    public static boolean show(Context ctx, AlertItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_OPEN_ALERTS, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle(item.getTitle())
                .setContentText(item.getMessage())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(item.getMessage()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManagerCompat.from(ctx).notify(item.getKey().hashCode(), builder.build());
        return true;
    }
}