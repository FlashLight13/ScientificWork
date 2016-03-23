package stydying.algo.com.algostudying.utils;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by Anton on 04.02.2016.
 */
public class BundleBuilder {

    private final Bundle bundle;

    public BundleBuilder() {
        this.bundle = new Bundle();
    }

    public BundleBuilder putLong(String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder putString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleBuilder putInt(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder putParcelable(String key, Parcelable parcelable) {
        bundle.putParcelable(key, parcelable);
        return this;
    }

    public BundleBuilder putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public Bundle build() {
        return bundle;
    }
}
