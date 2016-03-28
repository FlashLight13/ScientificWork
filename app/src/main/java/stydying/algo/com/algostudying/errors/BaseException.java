package stydying.algo.com.algostudying.errors;

import android.support.annotation.StringRes;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 04.02.2016.
 */
public class BaseException extends Exception {

    public static final String EMPTY_TASKS_TYPE = "TYPE_EMPTY_TASKS";
    public static final String NO_PLAYER = "NO_PLAYER";

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
        switch (type) {
            case BaseException.EMPTY_TASKS_TYPE:
                return R.string.error_no_tasks;
            case BaseException.NO_PLAYER:
                return R.string.error_no_player;
            default:
                return DEFAULT_MESSAGE_RES;
        }
    }
}
