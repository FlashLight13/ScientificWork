package stydying.algo.com.algostudying.data.entities.stats;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import stydying.algo.com.algostudying.data.Database;

/**
 * Created by Anton on 25.01.2016.
 */
@Table(database = Database.class)
public class Stat extends BaseModel implements Parcelable {

    public static final long DEFAULT_ID = -1;

    public enum Status {
        SYNCED, PENDING
    }

    @PrimaryKey(autoincrement = true)
    protected long id = DEFAULT_ID;
    @Column
    protected String groupName;
    @Column
    protected String taskName;
    @Column
    protected long time;
    @Column
    protected int commandsCount;
    @Column
    protected long taskId;
    @Column
    protected long taskGroupId;
    @Column
    protected Status status = Status.PENDING;
    @Column
    protected String userLogin;

    public Stat() {
    }

    public long getId() {
        return id;
    }

    public Stat setId(long id) {
        this.id = id;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public Stat setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public Stat setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public long getTime() {
        return time;
    }

    public Stat setTime(long time) {
        this.time = time;
        return this;
    }

    public int getCommandsCount() {
        return commandsCount;
    }

    public Stat setCommandsCount(int commandsCount) {
        this.commandsCount = commandsCount;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Stat setStatus(Status status) {
        this.status = status;
        return this;
    }

    public long getTaskId() {
        return taskId;
    }

    public Stat setTaskId(long taskId) {
        this.taskId = taskId;
        return this;
    }

    public long getTaskGroupId() {
        return taskGroupId;
    }

    public Stat setTaskGroupId(long taskGroupId) {
        this.taskGroupId = taskGroupId;
        return this;
    }

    public User getUser() {
        return new Select().from(User.class).where(User_Table.login.eq(userLogin)).querySingle();
    }

    public Stat setUserForeignKey(String login) {
        this.userLogin = login;
        return this;
    }

    public Stat setUserForeignKey(User user) {
        this.userLogin = user.getLogin();
        return this;
    }

    public static final Creator<Stat> CREATOR = new Creator<Stat>() {
        @Override
        public Stat createFromParcel(Parcel source) {
            return new Stat(source);
        }

        @Override
        public Stat[] newArray(int size) {
            return new Stat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private Stat(Parcel parcel) {
        id = parcel.readLong();
        groupName = parcel.readString();
        taskName = parcel.readString();
        time = parcel.readLong();
        commandsCount = parcel.readInt();
        taskId = parcel.readLong();
        taskGroupId = parcel.readLong();
        userLogin = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(groupName);
        dest.writeString(taskName);
        dest.writeLong(time);
        dest.writeInt(commandsCount);
        dest.writeLong(taskId);
        dest.writeLong(taskGroupId);
        dest.writeString(userLogin);
    }
}
