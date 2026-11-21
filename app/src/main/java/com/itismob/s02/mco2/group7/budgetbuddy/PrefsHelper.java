package com.itismob.s02.mco2.group7.budgetbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class PrefsHelper {

    private static final String PREF_FILE = "budgetbuddy_prefs";
    private static final String KEY_NAME = "KEY_NAME";
    private static final String KEY_EMAIL = "KEY_EMAIL";

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static void saveName(Context ctx, String name) {
        prefs(ctx).edit().putString(KEY_NAME, name).apply();
    }

    public static String getName(Context ctx) {
        return prefs(ctx).getString(KEY_NAME, "");
    }

    public static void saveEmail(Context ctx, String email) {
        prefs(ctx).edit().putString(KEY_EMAIL, email).apply();
    }

    public static String getEmail(Context ctx) {
        return prefs(ctx).getString(KEY_EMAIL, "");
    }

    private static final String KEY_NOTIFIED = "KEY_NOTIFIED";

    public static boolean wasNotified(Context ctx, String key) {
        return prefs(ctx).getStringSet(KEY_NOTIFIED, new HashSet<>()).contains(key);
    }

    public static void markNotified(Context ctx, String key) {
        Set<String> copy = new HashSet<>(prefs(ctx).getStringSet(KEY_NOTIFIED, new HashSet<>()));
        copy.add(key);
        prefs(ctx).edit().putStringSet(KEY_NOTIFIED, copy).apply();
    }
}