package stydying.algo.com.algostudying.errors;

import android.support.annotation.StringRes;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 04.02.2016.
 */
public class BaseException extends Exception {
    public static final String ERROR_EXECUTING_ALGO = "ERROR_EXECUTING_ALGO";
    public static final String ERROR_NOT_ALL_COLLECTED = "ERROR_NOT_ALL_COLLECTED";

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
        switch (type) {
            case ERROR_EXECUTING_ALGO:
                return R.string.error_cant_move_there;
            case ERROR_NOT_ALL_COLLECTED:
                return R.string.error_not_all_spheres_are_collected;
        }
        return DEFAULT_MESSAGE_RES;
    }
}
