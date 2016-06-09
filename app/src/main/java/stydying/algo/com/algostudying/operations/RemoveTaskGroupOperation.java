package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;

import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 16.02.2016.
 */
public class RemoveTaskGroupOperation implements OperationProcessingService.Operation {

    private Long id;

    public RemoveTaskGroupOperation() {
    }

    public RemoveTaskGroupOperation(TaskGroup taskGroup) {
        this.id = taskGroup.getId();
    }

    @Override
    public Object loadFromNetwork(Context context) throws NetworkException {
        TasksService.removeTaskGroup(id);
        new Delete().from(TaskGroup.class).where(TaskGroup_Table._id.eq(id)).execute();
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
