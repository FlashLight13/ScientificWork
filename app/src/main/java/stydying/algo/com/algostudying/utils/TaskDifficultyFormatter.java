package stydying.algo.com.algostudying.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 16.05.2016.
 */
public class TaskDifficultyFormatter {

    @Nullable
    public CharSequence getDifficulty(@NonNull Context context, int difficultyLevel) {
        int levelRes = R.string.label_task_difficulty_level_unknown;
        int levelColorRes = R.color.light_grey;
        switch (difficultyLevel) {
            case 1:
                levelRes = R.string.label_task_difficulty_level_easy;
                levelColorRes = R.color.green;
                break;
            case 2:
                levelRes = R.string.label_task_difficulty_level_medium;
                levelColorRes = R.color.yellow;
                break;
            case 3:
                levelRes = R.string.label_task_difficulty_level_hard;
                levelColorRes = R.color.red;
                break;
        }
        String levelDifValue = context.getString(levelRes);
        String resultString = context.getString(R.string.label_task_difficulty_level, levelDifValue);
        SpannableStringBuilder resultBuilder = new SpannableStringBuilder(resultString);
        resultBuilder.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(context, levelColorRes)),
                resultString.length() - levelDifValue.length(),
                resultString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return resultBuilder;
    }
}
