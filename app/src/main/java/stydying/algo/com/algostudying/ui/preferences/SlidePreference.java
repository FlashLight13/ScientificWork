package stydying.algo.com.algostudying.ui.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Anton on 04.05.2016.
 */
public class SlidePreference extends Preference {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SlidePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlidePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidePreference(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
    }
}
