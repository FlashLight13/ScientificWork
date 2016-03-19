package stydying.algo.com.algostudying.operations.edit_operations;

import java.util.ArrayList;
import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.operations.OperationProcessor;

/**
 * Created by Anton on 19.03.2016.
 */
public abstract class BaseEditOperation<T> implements OperationProcessor.Operation<T> {

    protected List<String> userIds;

    public BaseEditOperation() {
        this.userIds = new ArrayList<>();
    }

    public BaseEditOperation(List<User> users) {
        this.userIds = new ArrayList<>(users.size());
        for (User user : users) {
            userIds.add(user.getLogin());
        }
    }
}
