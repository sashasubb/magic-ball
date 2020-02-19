package com.example.ball.ball;

import android.content.Context;

import androidx.annotation.ArrayRes;

import com.example.ball.R;

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
            case 1:
                return getAnswer(R.array.indecisively_positive);
            case 2:
                return getAnswer(R.array.neutral);
            case 3:
                return getAnswer(R.array.negative);
            default:
                return getAnswer(R.array.positive);
        }
    }

    private String getAnswer(@ArrayRes int answerArrayRes) {
        int value = generator.nextInt(ANSWER_IN_ARRAY_COUNT - 1);
        return context.getResources().getStringArray(answerArrayRes)[value];
    }
}
