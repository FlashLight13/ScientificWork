package stydying.algo.com.algostudying.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import stydying.algo.com.algostudying.analytics.Events.AnalitycsEvent;

/**
 * Created by Anton on 04.06.2015.
 */
public class GoogleAnalyticsTracker implements AnalitycsTracker {

    GoogleAnalytics googleAnalytics;
    Tracker tracker;

    @Override
    public void init(Context context) {
        googleAnalytics = GoogleAnalytics.getInstance(context);

        googleAnalytics.setLocalDispatchPeriod(1800);

        tracker = googleAnalytics.newTracker("UA-XXXXX-Y"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    @Override
    public void trackEvent(AnalitycsEvent event) {
        tracker.send(event.buildEvent());
    }
}
