package stydying.algo.com.algostudying.operations.edit_operations;

import java.util.ArrayList;
import java.util.List;

import stydying.algo.com.algostudying.operations.OperationProcessingService;

/**
 * Created by Anton on 19.03.2016.
 */
public abstract class BaseEditOperation<T> implements OperationProcessingService.Operation<T> {

    protected List<String> userIds;

    public BaseEditOperation() {
        this.userIds = new ArrayList<>();
    }

    public BaseEditOperation(List<String> users) {
        this.userIds = users;
    }
}
