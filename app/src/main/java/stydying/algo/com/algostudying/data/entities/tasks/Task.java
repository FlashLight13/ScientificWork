package stydying.algo.com.algostudying.data.entities.tasks;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Comparator;

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
    protected long id = DEFAULT_ID;
    @Column
    protected String title;
    @Column
    protected String description;
    @Column
    protected int difficultyLevel;
    @Column
    protected long taskGroupId;

    transient private TaskGroup taskGroup;

    private String[][][] gameField;

    public Task() {
        super();
    }

    public Task(String title, TaskGroup taskGroup, String description, int width, int height, int difficultyLevel) {
        super();
        this.title = title;
        this.description = description;
        setTaskGroup(taskGroup);
        initEmptyGameField(width, height);
        this.difficultyLevel = difficultyLevel;
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

    public Task(Parcel parcel) {
        super();
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.taskGroupId = parcel.readLong();
        this.difficultyLevel = parcel.readInt();
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public Task setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        return this;
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
        if (taskGroup == null) {
            this.taskGroup = new Select().from(TaskGroup.class)
                    .where(TaskGroup_Table._id.eq(taskGroupId))
                    .querySingle();
        }
        return taskGroup;
    }

    public Task saveMap(Context context) {
        MapCache.getInstance(context).put(String.valueOf(id), gameField);
        return this;
    }

    public Task initMap(Context context) {
        if (gameField == null) {
            gameField = MapCache.getInstance(context).get(String.valueOf(id));
        }
        return this;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
        this.taskGroupId = taskGroup.getId();
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
        dest.writeLong(taskGroupId);
        dest.writeInt(difficultyLevel);
    }

    public static final class ByTaskDifficultyComparator implements Comparator<Task> {

        @Override
        public int compare(@Nullable Task lhs, @Nullable Task rhs) {
            if (lhs == null || rhs == null) {
                return 0;
            }
            return lhs.difficultyLevel - rhs.difficultyLevel;
        }
    }
}