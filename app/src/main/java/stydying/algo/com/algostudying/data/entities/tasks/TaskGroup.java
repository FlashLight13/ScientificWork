package stydying.algo.com.algostudying.data.entities.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.ArrayList;
import java.util.List;

import stydying.algo.com.algostudying.data.Database;
import stydying.algo.com.algostudying.data.entities.stats.User;

/**
 * Created by Anton on 25.01.2016.
 */
@ModelContainer
@Table(database = Database.class)
public class TaskGroup extends BaseModel implements Parcelable {

    public static final long DEFAULT_ID = -1;

    @SerializedName("id")
    @PrimaryKey
    long _id = DEFAULT_ID;
    @Column
    String title;
    List<Task> tasks;
    @ForeignKey
    transient ForeignKeyContainer<User> userForeignKeyContainer;

    public TaskGroup() {
    }

    public TaskGroup(String title, ArrayList<Task> tasks) {
        this.title = title;
        this.tasks = tasks;
    }

    public TaskGroup(String title) {
        this.title = title;
        this.tasks = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private TaskGroup(Parcel parcel) {
        _id = parcel.readLong();
        title = parcel.readString();
        tasks = parcel.readArrayList(Task.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public TaskGroup setTitle(String title) {
        this.title = title;
        return this;
    }

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "tasks")
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = SQLite.select()
                    .from(Task.class)
                    .where(Task_Table.taskGroupForeignKeyContainer__id.eq(_id))
                    .queryList();
        }
        return tasks;
    }

    public TaskGroup setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public User getUser() {
        return userForeignKeyContainer.load();
    }

    public TaskGroup setUserForeignKeyContainer(User user) {
        userForeignKeyContainer = FlowManager.getContainerAdapter(User.class)
                .toForeignKeyContainer(user);
        return this;
    }

    @Override
    public String toString() {
        return title;
    }

    public static final Creator<TaskGroup> CREATOR = new Creator<TaskGroup>() {
        @Override
        public TaskGroup createFromParcel(Parcel source) {
            return new TaskGroup(source);
        }

        @Override
        public TaskGroup[] newArray(int size) {
            return new TaskGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(title);
        dest.writeList(tasks);
    }
}
