package stydying.algo.com.algostudying.errors;

import android.support.annotation.StringRes;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 04.02.2016.
 */
public class BaseException extends Exception {

    protected final int DEFAULT_MESSAGE_RES = -1;

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
    public int message() {
        int messageRes = getMessageRes();
        if (messageRes != DEFAULT_MESSAGE_RES) {
            return messageRes;
        }
        return R.string.error_unknown;
    }

    @StringRes
    protected int getMessageRes() {
        return -1;
    }
}
