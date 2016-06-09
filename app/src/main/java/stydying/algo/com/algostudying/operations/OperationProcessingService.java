package stydying.algo.com.algostudying.operations;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import stydying.algo.com.algostudying.AlgoApplication;
import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.utils.SystemUtils;

/**
 * Created by Anton on 03.02.2016.
 */
public class OperationProcessingService extends IntentService {

    private static final String LOG_TAG = "OperationProcessService";

    private static final String OPERATION_EXTRA
            = "stydying.algo.com.algostudying.operations.OperationProcessingService.OPERATION_EXTRA";
    private static final String OPERATION_CLASS_EXTRA
            = "stydying.algo.com.algostudying.operations.OperationProcessingService.OPERATION_CLASS_EXTRA";

    public OperationProcessingService() {
        super("OperationProcessingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Operation operation = getOperation(intent);
        if (operation != null) {
            try {
                Object result;
                switch (operation.type()) {
                    case CACHE:
                        result = OperationsManager.get(this).shouldLoadFromNetwork(this, operation)
                                ? operation.loadFromNetwork(this)
                                : operation.loadFromLocal(this);
                        break;
                    case LOCAL_ONLY:
                        result = operation.loadFromLocal(this);
                        break;
                    case NETWORK_ONLY:
                        result = operation.loadFromNetwork(this);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operation type");
                }
                postEvent(new OperationSuccessEvent(operation.getClass(), result));
                OperationsManager.get(this).handleOperationExecuted(operation);
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
        Intent intent = new Intent(context, OperationProcessingService.class);
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

    public interface Operation<T> {

        enum OperationType {
            CACHE, LOCAL_ONLY, NETWORK_ONLY
        }

        T loadFromNetwork(Context context) throws NetworkException;

        T loadFromLocal(Context context);

        OperationType type();
    }

    public static final class OperationsManager {

        private static final long FROM_NETWORK_DELAY = TimeUnit.MINUTES.toMillis(2);

        private Map<Class<? extends Operation>, Long> operationTimes;

        public OperationsManager() {
            this.operationTimes = new HashMap<>();
        }

        private boolean shouldLoadFromNetwork(Context context, Operation operation) {
            Long lastNetworkExecutionTime = operationTimes.get(operation.getClass());
            return SystemUtils.isNetworkAvailable(context) && (lastNetworkExecutionTime == null
                    || lastNetworkExecutionTime - System.currentTimeMillis() > FROM_NETWORK_DELAY);
        }

        public void resetDelayForOperation(Class<? extends Operation> operationClass) {
            if (operationTimes.containsKey(operationClass)) {
                operationTimes.remove(operationClass);
            }
        }

        public void onLogout() {
            operationTimes.clear();
        }

        private void handleOperationExecuted(Operation operation) {
            operationTimes.put(operation.getClass(), System.currentTimeMillis());
        }

        public static OperationsManager get(Context context) {
            return ((AlgoApplication) context.getApplicationContext()).getOperationsManager();
        }
    }
}
