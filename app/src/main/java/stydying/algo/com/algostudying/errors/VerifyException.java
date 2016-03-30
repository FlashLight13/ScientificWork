package stydying.algo.com.algostudying.errors;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 05.02.2016.
 */
public class VerifyException extends BaseException {

    public static final String EMPTY_TASKS_TYPE = "TYPE_EMPTY_TASKS";
    public static final String NO_PLAYER = "NO_PLAYER";

    public VerifyException() {
        super();
    }

    public VerifyException(Throwable throwable) {
        super(throwable);
    }

    public VerifyException(String type) {
        super(type);
    }

    @Override
    public int getMessageRes() {
        switch (type) {
            case EMPTY_TASKS_TYPE:
                return R.string.error_no_tasks;
            case NO_PLAYER:
                return R.string.error_no_player;
            default:
                return super.getMessageRes();
        }
    }
}
