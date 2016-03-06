package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 29.02.2016.
 */
public class GetTaskOperation implements OperationProcessor.Operation {

    private long taskId;
    // todo remove this hack
    private long taskGroupId;

    public GetTaskOperation() {
    }

    public GetTaskOperation(Task task) {
        this.taskId = task.getId();
        this.taskGroupId = task.getTaskGroup().getId();
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        Task task = TasksService.getTask(this.taskId);
        task.saveMap(context);
        task.setTaskGroup(
                new Select().from(TaskGroup.class).where(TaskGroup_Table._id.eq(taskGroupId)).querySingle());
        return task;
    }
}
