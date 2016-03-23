package stydying.algo.com.algostudying.operations.edit_operations;

import android.content.Context;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 19.03.2016.
 */
public class EditTaskGroupOperation extends BaseEditOperation<TaskGroup> {

    private String title;
    private long id;

    public EditTaskGroupOperation() {
    }

    public EditTaskGroupOperation(long id, String newTitle, List<String> users) {
        super(users);
        this.title = newTitle;
        this.id = id;
    }

    @Override
    public TaskGroup loadFromNetwork(Context context) throws NetworkException {
        TaskGroup updatedTaskGroup = TasksService.updateTaskGroup(title, id, userIds);
        updatedTaskGroup.update();
        return updatedTaskGroup;
    }

    @Override
    public TaskGroup loadFromLocal(Context context) {
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.NETWORK_ONLY;
    }
}
