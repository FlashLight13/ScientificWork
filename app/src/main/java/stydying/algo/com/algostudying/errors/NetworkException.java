package stydying.algo.com.algostudying.errors;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 03.02.2016.
 */
public class NetworkException extends BaseException {

    private static final String FAILED_TO_CONNECT_TYPE = "FAILED_TO_CONNECT";

    public NetworkException() {
        super();
    }

    public NetworkException(Throwable throwable) {
        super(throwable);
    }

    public NetworkException(String type) {
        super(type);
    }

    @Override
    protected int getMessageRes() {
        switch (type) {
            case "SUCH_USER_ALREADY_EXISTS":
                return R.string.error_user_exists;
            case "WRONG_PASS":
                return R.string.error_wrong_pass;
            case "NO_SUCH_USER":
                return R.string.error_no_user;
            case FAILED_TO_CONNECT_TYPE:
                return R.string.error_failed_to_connect;
        }
        return DEFAULT_MESSAGE_RES;
    }

    public static NetworkException failedToConnect() {
        return new NetworkException(FAILED_TO_CONNECT_TYPE);
    }
}
