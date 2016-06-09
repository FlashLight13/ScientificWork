package stydying.algo.com.algostudying.logic.creation;

import android.content.Context;
import android.os.Parcel;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.CreateTaskOperation;
import stydying.algo.com.algostudying.utils.caches.BaseFileCache;
import stydying.algo.com.algostudying.utils.caches.MapCache;

/**
 * Created by Anton on 22.03.2016.
 */
public class GameFieldCreationController {

    private static final String GAME_FILED_CACHE_KEY = GameFieldCreationController.class.getName() + "GAME_FILED_CACHE_KEY";
    private static final String GAME_FILED_MAP_CACHE_KEY = GameFieldCreationController.class.getName() + "GAME_FILED_MAP_CACHE_KEY";

    private static GameFieldCreationController instance;

    private Task currentTask;
    private List<String> users;

    private GameFieldCreationController(Context context) {
        byte[] bytes = BaseFileCache.getInstance(context).get(GAME_FILED_CACHE_KEY);
        if (bytes != null && bytes.length > 0) {
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes, 0, bytes.length);
            currentTask = new Task(parcel);
            parcel.recycle();
        }
        if (currentTask != null) {
            String[][][] map = MapCache.getInstance(context).get(GAME_FILED_MAP_CACHE_KEY);
            currentTask.setGameField(map);
        }
    }

    public void init(Context context, Task task) {
        this.currentTask = task;
        Parcel parcel = Parcel.obtain();
        task.writeToParcel(parcel, 0);
        BaseFileCache.getInstance(context).put(GAME_FILED_CACHE_KEY, parcel.createByteArray());
        parcel.recycle();
        setGameField(context, task.getGameField());
    }

    public boolean shouldSelectAvailableStudents() {
        // select available students only for new TaskGroups
        return this.currentTask.getTaskGroup().getId() == TaskGroup.DEFAULT_ID;
    }

    public GameFieldCreationController setGameField(Context context, String[][][] gameField) {
        this.currentTask.setGameField(gameField);
        MapCache.getInstance(context).put(GAME_FILED_MAP_CACHE_KEY, gameField);
        return this;
    }

    public GameFieldCreationController setUsers(List<String> users) {
        this.users = users;
        return this;
    }

    public Task getTask() {
        return this.currentTask;
    }

    public boolean onSuccess(Context context, OperationSuccessEvent event) {
        if (event.isOperation(CreateTaskOperation.class)) {
            reset(context);
            return true;
        }
        return false;
    }

    public boolean onError(OperationErrorEvent event) {
        return event.isOperation(CreateTaskOperation.class);
    }

    private void reset(Context context) {
        MapCache.getInstance(context).clear();
        BaseFileCache.getInstance(context).clear();
        this.currentTask = null;
    }

    public CreateTaskOperation getCreateOperation() {
        return new CreateTaskOperation(currentTask, users);
    }

    public static GameFieldCreationController getInstance(Context context) {
        if (instance == null) {
            instance = new GameFieldCreationController(context);
        }
        return instance;
    }
}
