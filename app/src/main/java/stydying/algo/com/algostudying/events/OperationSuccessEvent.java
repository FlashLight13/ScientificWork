package stydying.algo.com.algostudying.events;

import stydying.algo.com.algostudying.operations.OperationProcessingService;

/**
 * Created by Anton on 03.02.2016.
 */
public class OperationSuccessEvent extends BaseOperationEvent {

    private final Object data;

    public OperationSuccessEvent(Class<? extends OperationProcessingService.Operation> operationClass, Object data) {
        super(operationClass);
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T data() {
        return (T) data;
    }
}
