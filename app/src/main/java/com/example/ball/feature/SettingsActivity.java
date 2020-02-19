package com.example.ball.feature;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ball.R;
import com.example.ball.settings.IPreferenceRepository;
import com.example.ball.settings.SettingsRepository;

public class SettingsActivity extends AppCompatActivity {

    public static final String SETTINGS_CLICK_KEY = "click_key";

    private TextView privacyPolicy;
    private Switch settingsSwitch;

    private IPreferenceRepository preferenceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initRepository();
        settingsSwitch = findViewById(R.id.settings_switch);
        privacyPolicy = findViewById(R.id.privacy_policy);
        settingsSwitch.setOnCheckedChangeListener((view, isCheck) ->
                this.settingsSwitch.setText(isCheck
                        ? R.string.settings_activity_click
                        : R.string.settings_activity_shake
                )
        );
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
    }

    private void prepareViews() {
        boolean isClick = preferenceRepository.getValue(SETTINGS_CLICK_KEY, true);
        settingsSwitch.setChecked(isClick);
        settingsSwitch.setText(isClick ? R.string.settings_activity_click : R.string.settings_activity_shake);
    }

    private void initRepository() {
        preferenceRepository = new SettingsRepository(this);
    }

    private void saveSettings() {
        boolean isClick = settingsSwitch.isChecked();
        preferenceRepository.setValue(SETTINGS_CLICK_KEY, isClick);
    }

}
