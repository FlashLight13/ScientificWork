package stydying.algo.com.algostudying.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Anton on 15.02.2016.
 */
public class SwipeControlledViewPager extends ViewPager {

    private boolean isSwipeEnabled = true;

    public SwipeControlledViewPager(Context context) {
        super(context);
    }

    public SwipeControlledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSwipeEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSwipeEnabled && super.onTouchEvent(ev);
    }

    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }

    public SwipeControlledViewPager setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
        return this;
    }
}
