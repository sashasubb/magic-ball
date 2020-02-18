package com.example.magicball.ball;

import android.content.Context;

import com.example.magicball.R;

import java.util.Random;

public class MagicResultImpl implements IMagicResult {

    private static final int ANSWER_BLOCK_COUNT = 4;
    private static final int ANSWER_IN_ARRAY_COUNT = 5;

    private Context context;
    private static Random generator;

    public MagicResultImpl(Context context) {
        this.context = context;
        generator = new Random();
    }

    @Override
    public String getResultId() {
        int value = generator.nextInt(ANSWER_BLOCK_COUNT - 1);
        switch (value) {
            case 1: return indecisivelyPositive();
            case 2: return neutral();
            case 3: return negative();
            default: return positive();
        }
    }

    private String positive() {
        int value = generator.nextInt(ANSWER_IN_ARRAY_COUNT - 1);
        return context.getResources().getStringArray(R.array.positive)[value];
    }

    private String indecisivelyPositive() {
        int value = generator.nextInt(ANSWER_IN_ARRAY_COUNT - 1);
        return context.getResources().getStringArray(R.array.indecisively_positive)[value];
    }

    private String neutral() {
        int value = generator.nextInt(ANSWER_IN_ARRAY_COUNT - 1);
        return context.getResources().getStringArray(R.array.neutral)[value];
    }

    private String negative() {
        int value = generator.nextInt(ANSWER_IN_ARRAY_COUNT - 1);
        return context.getResources().getStringArray(R.array.negative)[value];
    }
}
