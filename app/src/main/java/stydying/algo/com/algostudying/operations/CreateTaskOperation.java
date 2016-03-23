package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;


import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.stats.User_Table;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.interfaces.TaskInterface;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 16.02.2016.
 */
public class CreateTaskOperation implements OperationProcessor.Operation {

    private Task task;
    private TaskGroup taskGroup;
    private List<String> users;

    public CreateTaskOperation() {
    }

    public CreateTaskOperation(Task task, List<String> users) {
        this.task = task;
        this.taskGroup = task.getTaskGroup();
        this.users = users;
    }

    @Override
    public Object loadFromNetwork(Context context) throws NetworkException {
        TaskInterface.WorldData data = TasksService.createUpdateTask(
                new TaskInterface.WorldData(taskGroup, task, users));
        updateUsers(data.taskGroup, data.userIds);
        updateTaskGroup(data.taskGroup);
        data.task.setTaskGroup(data.taskGroup);
        data.task.save();
        return null;
    }

    private void updateTaskGroup(TaskGroup taskGroup) {
        TaskGroup existedTaskGroup = new Select()
                .from(TaskGroup.class)
                .where(TaskGroup_Table._id.eq(taskGroup.getId()))
                .querySingle();
        if (existedTaskGroup == null) {
            taskGroup.save();
        } else {
            taskGroup.update();
        }
    }

    private void updateUsers(TaskGroup taskGroup, List<String> users) {
        for (String login : users) {
            User user = new Select().from(User.class).where(User_Table.login.eq(login)).querySingle();
            if (user != null) {
                user.getTaskGroups().add(taskGroup);
                user.update();
            }
        }
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
