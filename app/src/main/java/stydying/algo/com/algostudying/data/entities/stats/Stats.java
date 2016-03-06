package stydying.algo.com.algostudying.data.entities.stats;

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

/**
 * Created by Anton on 25.01.2016.
 */
@Table(database = Database.class)
public class Stats extends BaseModel implements Parcelable {

    @PrimaryKey
    long id;
    @Column
    String groupName;
    @Column
    String taskName;
    @Column
    long averageTime;
    @Column
    int triesCount;
    @Column
    int commandsCount;
    @ForeignKey
    transient ForeignKeyContainer<User> userForeignKeyContainer;

    public Stats() {
    }

    public Stats(String groupName, String taskName, long averageTime, int triesCount, int commandsCount) {
        this.groupName = groupName;
        this.taskName = taskName;
        this.averageTime = averageTime;
        this.triesCount = triesCount;
        this.commandsCount = commandsCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public Stats setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getTaskName() {
        return taskName;
    }

    public Stats setTaskName(String taskName) {
        this.taskName = taskName;
        return this;
    }

    public long getAverageTime() {
        return averageTime;
    }

    public Stats setAverageTime(long averageTime) {
        this.averageTime = averageTime;
        return this;
    }

    public int getTriesCount() {
        return triesCount;
    }

    public Stats setTriesCount(int triesCount) {
        this.triesCount = triesCount;
        return this;
    }

    public int getCommandsCount() {
        return commandsCount;
    }

    public Stats setCommandsCount(int commandsCount) {
        this.commandsCount = commandsCount;
        return this;
    }

    public User getUser() {
        return userForeignKeyContainer.load();
    }

    public Stats setUserForeignKeyContainer(User user) {
        userForeignKeyContainer = FlowManager.getContainerAdapter(User.class)
                .toForeignKeyContainer(user);
        return this;
    }

    public static final Creator<Stats> CREATOR = new Creator<Stats>() {
        @Override
        public Stats createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Stats[] newArray(int size) {
            return new Stats[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private Stats(Parcel parcel) {
        groupName = parcel.readString();
        taskName = parcel.readString();
        averageTime = parcel.readLong();
        triesCount = parcel.readInt();
        commandsCount = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupName);
        dest.writeString(taskName);
        dest.writeLong(averageTime);
        dest.writeInt(triesCount);
        dest.writeInt(commandsCount);
    }
}
