package com.example.magicball.settings;

public interface IPreferenceRepository {
    void setValue(String key, Boolean value);
    Boolean getValue(String key, Boolean defaultValue);
}
