package stydying.algo.com.algostudying.operations.edit_operations;

import android.content.Context;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 19.03.2016.
 */
public class EditTaskOperation extends BaseEditOperation {

    private Task task;

    public EditTaskOperation() {
    }

    public EditTaskOperation(Task editedTask, List<User> users) {
        super(users);
        this.task = editedTask;
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        return TasksService.updateTask(task, task.getTaskGroup().getId(), userIds);
    }
}
