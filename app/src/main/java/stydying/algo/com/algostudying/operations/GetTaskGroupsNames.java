package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 16.02.2016.
 */
public class GetTaskGroupsNames implements OperationProcessor.Operation {

    public GetTaskGroupsNames() {
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        List<TaskGroup> taskGroupList = TasksService.getTaskGroupsNames();
        for (TaskGroup taskGroup : taskGroupList) {
            createUpdate(taskGroup);
        }
        return taskGroupList;
    }

    private void createUpdate(TaskGroup taskGroup) {
        if (!taskGroup.exists()) {
            taskGroup.save();
        } else {
            taskGroup.update();
        }
        for (Task task : taskGroup.getTasks()) {
            task.setTaskGroup(taskGroup);
        }
    }
}
