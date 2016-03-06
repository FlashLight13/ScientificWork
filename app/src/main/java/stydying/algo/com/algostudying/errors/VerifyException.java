package stydying.algo.com.algostudying.errors;

/**
 * Created by Anton on 05.02.2016.
 */
public class VerifyException extends BaseException {

    public VerifyException() {
        super();
    }

    public VerifyException(Throwable throwable) {
        super(throwable);
    }

    public VerifyException(String type) {
        super(type);
    }
}
