package stydying.algo.com.algostudying.events;

import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.operations.OperationProcessingService;

/**
 * Created by Anton on 03.02.2016.
 */
public class OperationErrorEvent extends BaseOperationEvent {

    public final BaseException error;

    public OperationErrorEvent(Class<? extends OperationProcessingService.Operation> operationClass,
                               BaseException error) {
        super(operationClass);
        this.error = error;
    }
}
