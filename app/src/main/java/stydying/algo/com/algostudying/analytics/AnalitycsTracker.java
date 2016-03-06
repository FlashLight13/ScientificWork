package stydying.algo.com.algostudying.analytics;

import android.content.Context;

import stydying.algo.com.algostudying.analytics.Events.AnalitycsEvent;

/**
 * Created by Anton on 04.06.2015.
 */
public interface AnalitycsTracker {
    public void init(Context context);

    public void trackEvent(AnalitycsEvent event);
}
