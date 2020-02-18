package com.example.magicball.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsRepository implements IPreferenceRepository {

    public static final String APP_PREFERENCES = "mysettings";

    private final Context context;

    public SettingsRepository(Context context) {
        this.context = context;
    }

    @Override
    public void setValue(String key, Boolean value) {
        getPreference()
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    @Override
    public Boolean getValue(String key, Boolean defaultValue) {
        return getPreference().getBoolean(key, defaultValue);
    }

    private SharedPreferences getPreference() {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
