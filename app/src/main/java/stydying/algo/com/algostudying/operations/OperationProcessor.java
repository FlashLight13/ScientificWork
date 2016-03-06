package stydying.algo.com.algostudying.operations;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;

/**
 * Created by Anton on 03.02.2016.
 */
public class OperationProcessor extends IntentService {

    private static final String LOG_TAG = "OperationProcessor";

    private static final String OPERATION_EXTRA
            = "stydying.algo.com.algostudying.operations.OperationProcessor.OPERATION_EXTRA";
    private static final String OPERATION_CLASS_EXTRA
            = "stydying.algo.com.algostudying.operations.OperationProcessor.OPERATION_CLASS_EXTRA";


    public OperationProcessor() {
        super("OperationProcessor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Operation operation = getOperation(intent);
        if (operation != null) {
            try {
                Object result = operation.execute(this);
                postEvent(new OperationSuccessEvent(operation.getClass(), result));
            } catch (BaseException baseException) {
                Log.d(LOG_TAG, "Error on executing operation", baseException);
                postEvent(new OperationErrorEvent(operation.getClass(), baseException));
            } catch (Throwable throwable) {
                Log.d(LOG_TAG, "Error on executing operation", throwable);
                postEvent(new OperationErrorEvent(operation.getClass(), new BaseException(throwable)));
            }
        }
    }

    private void postEvent(final Object event) {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BusProvider.bus().post(event);
            }
        });
    }

    public static void executeOperation(Context context, Operation operation) {
        Intent intent = new Intent(context, OperationProcessor.class);
        intent.putExtra(OPERATION_EXTRA, new Gson().toJson(operation));
        intent.putExtra(OPERATION_CLASS_EXTRA, operation.getClass().getName());
        context.startService(intent);
    }

    private static Operation getOperation(Intent intent) {
        String operationJson = intent.getStringExtra(OPERATION_EXTRA);
        if (!TextUtils.isEmpty(operationJson)) {
            try {
                Class operationClass = Class.forName(intent.getStringExtra(OPERATION_CLASS_EXTRA));
                return (Operation) new Gson().fromJson(operationJson, operationClass);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public interface Operation {
        Object execute(Context context) throws NetworkException;
    }
}
