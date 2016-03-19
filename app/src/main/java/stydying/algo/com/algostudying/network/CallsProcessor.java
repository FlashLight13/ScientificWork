package stydying.algo.com.algostudying.network;

import android.support.annotation.NonNull;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.interfaces.TaskInterface;

/**
 * Created by Anton on 19.03.2016.
 */
public class CallsProcessor<T> {

    private static final String SERVER_URL = "http://192.168.1.225:8080/";
    private static final int TIMEOUT_SECS = 60;
    private static final int CONNECT_TIMEOUT_SECS = 20;
    private static final String LOG_TAG = "NetworkProvider";

    private static Retrofit retrofit;

    @SuppressWarnings("unchecked")
    public T executeCall(@NonNull Call<T> call) throws NetworkException {
        try {
            retrofit2.Response response = call.execute();
            if (response.isSuccessful()) {
                return (T) response.body();
            } else {
                throw new NetworkException(response.errorBody().string());
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

    public static TaskInterface taskService() {
        return retrofit().create(TaskInterface.class);
    }

    public static Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT_SECS, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
