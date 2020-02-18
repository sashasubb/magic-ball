package com.example.magicball;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.magicball.ball.IMagicResult;
import com.example.magicball.ball.MagicResultImpl;
import com.example.magicball.settings.IPreferenceRepository;
import com.example.magicball.settings.SettingsRepository;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final static int ANIMATION_DELAY = 4000;

    ImageView settingButton;
    FrameLayout magicBall;
    TextView answerText;
    ImageView textBack;

    private IMagicResult magicResult;
    private IPreferenceRepository preferenceRepository;
    private Handler handler = new Handler();
    Animation magicBoxAnim;
    private Runnable ballWorker = () -> {
        answerText.setText(magicResult.getResultId());
        textBack.setVisibility(View.VISIBLE);
    };
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private boolean isByClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceRepository = new SettingsRepository(this);
        magicResult = new MagicResultImpl(this);
        settingButton = findViewById(R.id.setting_button);
        textBack = findViewById(R.id.text_back);
        settingButton.setOnClickListener(this::onSettingsClick);
        magicBall = findViewById(R.id.magic_ball);
        answerText = findViewById(R.id.answer_text);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magicBall.setOnClickListener((view) -> {
            if (!isByClick) return;
           vibrator.vibrate(ANIMATION_DELAY);
            vibrator.vibrate(ANIMATION_DELAY);
            if (magicBoxAnim == null) {
                magicBoxAnim = AnimationUtils.loadAnimation(this, R.anim.magicbox);
            }
            magicBoxAnim.setDuration(ANIMATION_DELAY);
            magicBall.setAnimation(magicBoxAnim);
            magicBoxAnim.start();
            handler.removeCallbacks(ballWorker);
            handler.postDelayed(ballWorker, ANIMATION_DELAY);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        answerText.setText(R.string.default_answer_text);
        textBack.setVisibility(View.INVISIBLE);
        isByClick = preferenceRepository.getValue(SettingsActivity.SETTINGS_CLICK_KEY, true);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vibrator.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    private void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //TODO условия точности сенсора
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (isByClick) return;
        vibrator.vibrate(ANIMATION_DELAY);
        handler.removeCallbacks(ballWorker);
        handler.postDelayed(ballWorker, ANIMATION_DELAY);
    }
}
