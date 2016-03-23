package stydying.algo.com.algostudying.operations.edit_operations;

import android.content.Context;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 19.03.2016.
 */
public class EditTaskOperation extends BaseEditOperation<Task> {

    private Task task;

    public EditTaskOperation() {
    }

    public EditTaskOperation(Task editedTask, List<String> users) {
        super(users);
        this.task = editedTask;
    }

    @Override
    public Task loadFromNetwork(Context context) throws NetworkException {
        Task updatedTask = TasksService.updateTask(task, task.getTaskGroup().getId(), userIds);
        updatedTask.update();
        return updatedTask;
    }

    @Override
    public Task loadFromLocal(Context context) {
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.NETWORK_ONLY;
    }
}
