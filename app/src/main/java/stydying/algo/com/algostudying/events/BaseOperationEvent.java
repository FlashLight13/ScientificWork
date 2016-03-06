package stydying.algo.com.algostudying.events;

import stydying.algo.com.algostudying.operations.OperationProcessor;

/**
 * Created by Anton on 06.02.2016.
 */
public class BaseOperationEvent {

    private final Class<? extends OperationProcessor.Operation> operationClass;

    public BaseOperationEvent(Class<? extends OperationProcessor.Operation> operationClass) {
        this.operationClass = operationClass;
    }

    public boolean isOperation(Class<? extends OperationProcessor.Operation> operationClass) {
        return this.operationClass.equals(operationClass);
    }
}
