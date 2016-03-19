package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;


import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;
import stydying.algo.com.algostudying.data.entities.tasks.Task_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.interfaces.TaskInterface;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 16.02.2016.
 */
public class CreateUpdateTaskOperation implements OperationProcessor.Operation {

    private Task task;
    private TaskGroup taskGroup;

    public CreateUpdateTaskOperation() {
    }

    public CreateUpdateTaskOperation(Task task) {
        this.task = task;
        this.taskGroup = task.getTaskGroup();
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        TaskInterface.WorldData data = TasksService.createUpdateTask(new TaskInterface.WorldData(taskGroup, task));

        new Delete().from(Task.class).where(Task_Table.id.eq(Task.DEFAULT_ID)).execute();
        new Delete().from(TaskGroup.class).where(TaskGroup_Table._id.eq(TaskGroup.DEFAULT_ID)).execute();
        data.task.update();
        data.taskGroup.update();
        data.task.setTaskGroup(data.taskGroup);
        return null;
    }
}
