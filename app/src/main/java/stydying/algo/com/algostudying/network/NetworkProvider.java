package stydying.algo.com.algostudying.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.parsers.Parser;

/**
 * Created by Anton on 02.02.2016.
 */
public class NetworkProvider {

    private static final String LOG_TAG = "NetworkProvider";

    private static final String SERVER_URL = "http://algo-algostudying.rhcloud.com";
    private static final int TIMEOUT_SECS = 60;
    private static final int CONNECT_TIMEOUT_SECS = 20;

    private final Parser DEFAULT_PARSER = new Parser() {
        @Override
        public Object parse(ResponseBody responseBody) throws IOException {
            return null;
        }
    };

    private static NetworkProvider instance;

    private OkHttpClient client;

    public <T> T executePost(@NonNull HttpUrl url,
                             @NonNull RequestBody body,
                             @Nullable Headers headers,
                             @NonNull Parser<T> parser) throws NetworkException {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
            if (headers != null) {
                requestBuilder.headers(headers);
            }
            Response response = client.newCall(requestBuilder.build()).execute();
            if (response.isSuccessful()) {
                return parser.parse(response.body());
            } else {
                throw new NetworkException(response.body().string());
            }
        } catch (NetworkException ne) {
            throw ne;
        } catch (SocketTimeoutException socketTimeoutException) {
            throw NetworkException.failedToConnect();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Unknown error on executing request", e);
            throw new NetworkException();
        }
    }

    public void executePost(@NonNull HttpUrl url,
                            @NonNull RequestBody body,
                            @Nullable Headers headers) throws NetworkException {
        executePost(url, body, headers, DEFAULT_PARSER);
    }

    private NetworkProvider() {
        client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUrl createUrl(String baseUrl) {
        return HttpUrl.parse(SERVER_URL + baseUrl);
    }

    public static NetworkProvider getInstance() {
        if (instance == null) {
            instance = new NetworkProvider();
        }
        return instance;
    }
}
