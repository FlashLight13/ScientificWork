package stydying.algo.com.algostudying.data.entities.tasks;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import stydying.algo.com.algostudying.data.Database;
import stydying.algo.com.algostudying.game.objects.CubeBlock;
import stydying.algo.com.algostudying.game.objects.EmptyObject;
import stydying.algo.com.algostudying.game.objects.ObjectSerializator;
import stydying.algo.com.algostudying.utils.caches.MapCache;

/**
 * Created by Anton on 25.01.2016.
 */
@Table(database = Database.class)
public class Task extends BaseModel implements Parcelable {

    public static final long DEFAULT_ID = -1;
    public static final int MAX_GAME_WORLD_HEIGHT = 6;

    @PrimaryKey
    long id = DEFAULT_ID;
    @Column
    String title;
    @Column
    String description;
    @ForeignKey
    transient ForeignKeyContainer<TaskGroup> taskGroupForeignKeyContainer;

    private String[][][] gameField;

    public Task() {
        super();
    }

    public Task(String title, String[][][] gameField, String description) {
        super();
        this.title = title;
        this.gameField = gameField;
        this.description = description;
    }

    public Task(String title, TaskGroup taskGroup, String description, int width, int height) {
        super();
        this.title = title;
        this.description = description;
        setTaskGroup(taskGroup);
        initEmptyGameField(width, height);
    }

    private void initEmptyGameField(int width, int height) {
        this.gameField = new String[width][height][MAX_GAME_WORLD_HEIGHT];
        for (int _x = 0; _x < width; _x++) {
            for (int _y = 0; _y < height; _y++) {
                for (int _z = 0; _z < MAX_GAME_WORLD_HEIGHT; _z++) {
                    if (_z == 0) {
                        gameField[_x][_y][_z] = ObjectSerializator.toJsonString(CubeBlock.class);
                    } else {
                        gameField[_x][_y][_z] = ObjectSerializator.toJsonString(EmptyObject.class);
                    }
                }
            }
        }
    }

    public Task(Task task) {
        super();
        this.id = task.id;
        this.title = task.title;
        this.gameField = task.gameField;
        this.description = task.description;
    }

    private Task(Parcel parcel) {
        super();
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.description = parcel.readString();
        TaskGroup taskGroup = parcel.readParcelable(TaskGroup.class.getClassLoader());
        setTaskGroup(taskGroup);
    }

    public String[][][] getGameField() {
        return gameField;
    }

    public Task setGameField(String[][][] gameField) {
        this.gameField = gameField;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public long getId() {
        return id;
    }

    public Task setId(long id) {
        this.id = id;
        return this;
    }

    public TaskGroup getTaskGroup() {
        if (taskGroupForeignKeyContainer == null) {
            return null;
        }
        return taskGroupForeignKeyContainer.load();
    }

    public Task saveMap(Context context) {
        MapCache.getInstance(context).put(String.valueOf(id), gameField);
        return this;
    }

    public Task initMap(Context context) {
        gameField = MapCache.getInstance(context).get(String.valueOf(id));
        return this;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        taskGroupForeignKeyContainer = FlowManager.getContainerAdapter(TaskGroup.class)
                .toForeignKeyContainer(taskGroup);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(getTaskGroup(), flags);
    }
}
