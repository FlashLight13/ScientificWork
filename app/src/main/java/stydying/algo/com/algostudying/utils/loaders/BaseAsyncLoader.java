package stydying.algo.com.algostudying.utils.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by anton on 09.08.15.
 */
public abstract class BaseAsyncLoader<T> extends AsyncTaskLoader<T> {
    public BaseAsyncLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
