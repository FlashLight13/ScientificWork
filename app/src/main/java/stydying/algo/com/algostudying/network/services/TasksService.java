package stydying.algo.com.algostudying.network.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.NetworkProvider;
import stydying.algo.com.algostudying.network.parsers.EntityParser;
import stydying.algo.com.algostudying.utils.BaseJsonEntityBuilder;

/**
 * Created by Anton on 05.02.2016.
 */
public class TasksService {

    public static WorldData createUpdateTask(WorldData worldData) throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/createOrUpdateWorld"),
                RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(worldData)),
                null,
                new EntityParser<>(WorldData.class));
    }

    public static List<TaskGroup> getTaskGroupsNames() throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/getTaskGroupsNames"),
                RequestBody.create(null, ""),
                null,
                new EntityParser<>(new TypeToken<List<TaskGroup>>() {
                }));
    }

    public static void removeTaskGroup(Long id) throws NetworkException {
        String jsonRequest = new BaseJsonEntityBuilder().add("id", id).build();
        NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/removeTaskGroup"),
                RequestBody.create(MediaType.parse("application/json"), jsonRequest),
                null);
    }

    public static TaskGroup updateTaskGroup(TaskGroup taskGroup) throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/createOrUpdateWorld"),
                RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(taskGroup)),
                null,
                new EntityParser<>(TaskGroup.class));
    }

    public static Task getTask(Long taskId) throws NetworkException {
        String jsonRequest = new BaseJsonEntityBuilder().add("id", taskId).build();
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/task"),
                RequestBody.create(MediaType.parse("application/json"), jsonRequest),
                null,
                new EntityParser<>(Task.class));
    }

    public static final class WorldData {
        public TaskGroup taskGroup;
        public Task task;

        public WorldData() {
        }

        public WorldData(TaskGroup taskGroup, Task task) {
            this.taskGroup = taskGroup;
            this.task = task;
        }
    }
}
