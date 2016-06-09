package stydying.algo.com.algostudying.events;

import stydying.algo.com.algostudying.operations.OperationProcessingService;

/**
 * Created by Anton on 06.02.2016.
 */
public class BaseOperationEvent {

    private final Class<? extends OperationProcessingService.Operation> operationClass;

    public BaseOperationEvent(Class<? extends OperationProcessingService.Operation> operationClass) {
        this.operationClass = operationClass;
    }

    public boolean isOperation(Class<? extends OperationProcessingService.Operation> operationClass) {
        return this.operationClass.equals(operationClass);
    }
}
