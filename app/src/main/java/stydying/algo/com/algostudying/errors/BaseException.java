package stydying.algo.com.algostudying.errors;

import android.support.annotation.StringRes;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 04.02.2016.
 */
public class BaseException extends Exception {
    protected final int DEFAULT_MESSAGE_RES = R.string.error_unknown;

    protected String type;

    public BaseException() {
        this("");
    }

    public BaseException(Throwable throwable) {
        super(throwable);
        this.type = "";
    }

    public BaseException(String type) {
        this.type = type;
    }

    @StringRes
    public int getMessageRes() {
        return DEFAULT_MESSAGE_RES;
    }
}
