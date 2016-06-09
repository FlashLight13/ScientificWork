package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.Task_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 26.03.2016.
 */
public class RemoveTaskOperation implements OperationProcessingService.Operation {

    private long id;

    public RemoveTaskOperation() {
    }

    public RemoveTaskOperation(long id) {
        this.id = id;
    }

    @Override
    public Object loadFromNetwork(Context context) throws NetworkException {
        TasksService.removeTask(id);
        new Delete().from(Task.class).where(Task_Table.id.eq(id)).execute();
        return null;
    }

    @Override
    public Object loadFromLocal(Context context) {
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.NETWORK_ONLY;
    }
}
