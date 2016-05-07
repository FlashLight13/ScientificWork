package stydying.algo.com.algostudying.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by Anton on 04.05.2016.
 */
public class fd extends Preference {

    @TargetApi(21)
    public fd(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public fd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public fd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public fd(Context context) {
        super(context);
    }
}
