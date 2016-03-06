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
public class RemoveTaskGroup implements OperationProcessor.Operation {

    private Long id;

    public RemoveTaskGroup() {
    }

    public RemoveTaskGroup(TaskGroup taskGroup) {
        this.id = taskGroup.getId();
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        TasksService.removeTaskGroup(id);
        new Delete().from(TaskGroup.class).where(TaskGroup_Table._id.eq(id)).execute();
        return null;
    }
}
