package com.example.ball.settings;

public interface IPreferenceRepository {
    void setValue(String key, Boolean value);

    Boolean getValue(String key, Boolean defaultValue);
}
