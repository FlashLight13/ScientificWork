package stydying.algo.com.algostudying.network.parsers;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Anton on 04.02.2016.
 */
public interface Parser<T> {

    T parse(ResponseBody responseBody) throws IOException;
}
