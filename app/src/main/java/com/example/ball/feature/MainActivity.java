package com.example.ball.feature;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ball.R;
import com.example.ball.ball.IMagicResult;
import com.example.ball.ball.MagicResultImpl;
import com.example.ball.settings.IPreferenceRepository;
import com.example.ball.settings.SettingsRepository;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final static int ANIMATION_DELAY = 4000;

    private ImageView settingButton;
    private FrameLayout magicBall;
    private TextView answerText;
    private ImageView textBack;

    private IMagicResult magicResult;
    private IPreferenceRepository preferenceRepository;
    private Handler handler = new Handler();
    private Animation magicBoxAnim;
    private Runnable ballWorker = () -> {
        answerText.setText(magicResult.getResultId());
        textBack.setVisibility(View.VISIBLE);
        isRunning = false;
    };
    private Vibrator vibrator;
    private SensorManager sensorManager;
    private boolean isByClick;
    private volatile boolean isRunning;
    private static final float SHAKE_THRESHOLD = 3.25f;
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 5000;
    private long mLastShakeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRepo();
        initView();
        initComponent();
        settingButton.setOnClickListener(this::onSettingsClick);
        magicBall.setOnClickListener((view) -> {
            if (!isByClick || isRunning) return;
            magicBallAnim();
        });
    }

    private void magicBallAnim() {
        isRunning = true;
        if (magicBoxAnim == null) {
            magicBoxAnim = AnimationUtils.loadAnimation(this, R.anim.magicbox);
        }
        magicBoxAnim.setDuration(ANIMATION_DELAY);
        prepareViewBeforeAnimation();
        magicBall.startAnimation(magicBoxAnim);
        vibrate();
        handler.removeCallbacks(ballWorker);
        handler.postDelayed(ballWorker, ANIMATION_DELAY);
    }

    private void prepareViewBeforeAnimation() {
        answerText.setText(R.string.default_answer_text);
        textBack.setVisibility(View.INVISIBLE);
    }

    private void initRepo() {
        preferenceRepository = new SettingsRepository(this);
        magicResult = new MagicResultImpl(this);
    }

    private void initView() {
        settingButton = findViewById(R.id.setting_button);
        textBack = findViewById(R.id.text_back);
        magicBall = findViewById(R.id.magic_ball);
        answerText = findViewById(R.id.answer_text);
    }

    private void initComponent() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareViewBeforeAnimation();
        isRunning = false;
        mLastShakeTime = System.currentTimeMillis();
        isByClick = preferenceRepository.getValue(SettingsActivity.SETTINGS_CLICK_KEY, true);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        magicBall.clearAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vibrator.cancel();
        handler.removeCallbacks(ballWorker);
        sensorManager.unregisterListener(this);
    }

    private void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isByClick || isRunning) return;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    magicBallAnim();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (isByClick) return;
        magicBallAnim();
    }

    private void vibrate() {
        vibrator.vibrate(ANIMATION_DELAY);
    }
}
