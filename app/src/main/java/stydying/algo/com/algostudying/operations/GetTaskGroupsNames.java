package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 16.02.2016.
 */
public class GetTaskGroupsNames implements OperationProcessor.Operation<List<TaskGroup>> {

    public GetTaskGroupsNames() {
    }

    @Override
    public List<TaskGroup> loadFromNetwork(Context context) throws NetworkException {
        List<TaskGroup> taskGroupList = TasksService.getTaskGroupsNames();
        for (TaskGroup taskGroup : taskGroupList) {
            createUpdate(taskGroup);
        }
        return taskGroupList;
    }

    private void createUpdate(TaskGroup taskGroup) {
        for (Task task : taskGroup.getTasks()) {
            task.setTaskGroup(taskGroup);
            task.save();
        }
        taskGroup.save();
    }

    @Override
    public List<TaskGroup> loadFromLocal(Context context) {
        return new Select().from(TaskGroup.class).queryList();
    }

    @Override
    public OperationType type() {
        return OperationType.CACHE;
    }
}
