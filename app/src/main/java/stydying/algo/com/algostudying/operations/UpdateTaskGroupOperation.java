package stydying.algo.com.algostudying.operations;

import android.content.Context;

import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 22.02.2016.
 */
public class UpdateTaskGroupOperation implements OperationProcessor.Operation {

    private TaskGroup taskGroup;

    public UpdateTaskGroupOperation() {
    }

    public UpdateTaskGroupOperation(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        TaskGroup taskGroup = TasksService.updateTaskGroup(this.taskGroup);
        taskGroup.save();
        return taskGroup;
    }
}
