package stydying.algo.com.algostudying.network.services;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.CallsProcessor;
import stydying.algo.com.algostudying.network.interfaces.TaskInterface;

/**
 * Created by Anton on 05.02.2016.
 */
public class TasksService {

    private static TaskInterface api;

    public static TaskInterface.WorldData createUpdateTask(TaskInterface.WorldData worldData) throws NetworkException {
        return new CallsProcessor<TaskInterface.WorldData>().executeCall(api().createTask(worldData));
    }

    public static List<TaskGroup> getTaskGroupsNames() throws NetworkException {
        return new CallsProcessor<List<TaskGroup>>().executeCall(api().getTaskGroupsNames());
    }

    public static void removeTaskGroup(Long id) throws NetworkException {
        new CallsProcessor<Void>().executeCall(api().removeTaskGroup(
                new TaskInterface.BaseIdRequest(id)
        ));
    }

    public static TaskGroup updateTaskGroup(String title, Long id, List<String> userIds) throws NetworkException {
        return new CallsProcessor<TaskGroup>().executeCall(api().updateTaskGroup(
                new TaskInterface.UpdateTaskGroupData(title, id, userIds)));
    }

    public static Task updateTask(Task updatedTask, long taskGroupId, List<String> usersIds) throws NetworkException {
        return new CallsProcessor<Task>().executeCall(api().updateTask(
                new TaskInterface.UpdateTaskData(updatedTask, taskGroupId, usersIds)));
    }

    public static Task getTask(Long taskId) throws NetworkException {
        return new CallsProcessor<Task>().executeCall(api().getTask(
                new TaskInterface.BaseIdRequest(taskId)));
    }

    private static TaskInterface api() {
        if (api == null) {
            api = CallsProcessor.retrofit().create(TaskInterface.class);
        }
        return api;
    }
}
